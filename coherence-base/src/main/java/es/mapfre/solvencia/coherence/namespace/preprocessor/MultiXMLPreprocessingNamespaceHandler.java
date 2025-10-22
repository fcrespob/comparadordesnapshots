/*
 * File: XmlPreprocessingNamespaceHandler.java
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * The contents of this file are subject to the terms and conditions of 
 * the Common Development and Distribution License 1.0 (the "License").
 *
 * You may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License by consulting the LICENSE.txt file
 * distributed with this file, or by consulting https://oss.oracle.com/licenses/CDDL
 *
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file LICENSE.txt.
 *
 * MODIFICATIONS:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 */

package es.mapfre.solvencia.coherence.namespace.preprocessor;

import java.net.URI;
import java.text.ParseException;
import java.util.HashSet;

import com.oracle.coherence.common.namespace.preprocessing.XmlPreprocessingNamespaceHandler;
import com.tangosol.config.ConfigurationException;
import com.tangosol.config.expression.Expression;
import com.tangosol.config.expression.ExpressionParser;
import com.tangosol.config.xml.DocumentElementPreprocessor;
import com.tangosol.config.xml.DocumentElementPreprocessor.ElementPreprocessor;
import com.tangosol.config.xml.ElementProcessor;
import com.tangosol.config.xml.NamespaceHandler;
import com.tangosol.config.xml.ProcessingContext;
import com.tangosol.run.xml.QualifiedName;
import com.tangosol.run.xml.XmlElement;
import com.tangosol.run.xml.XmlHelper;
import com.tangosol.run.xml.XmlValue;

/**
 * The {@link MultiXMLPreprocessingNamespaceHandler} provides specialized {@link XmlElement}
 * pre and post processing capabilities for configurations files, useful for
 * transforming {@link XmlElement}s on the fly.
 * <p>
 * Copyright (c) 2013. All Rights Reserved. Oracle Corporation.<br>
 * Oracle is a registered trademark of Oracle Corporation and/or its affiliates.
 * <p>
 * "Portions Copyright 2014 Indra"
 *
 * @author Brian Oliver
 * @author Jonathan Knight
 */
public class MultiXMLPreprocessingNamespaceHandler extends XmlPreprocessingNamespaceHandler
{
    /**
     * The 'introduce-cache-config' {@link XmlElement}.
     */
    private static final String INTRODUCE_CACHE_CONFIG = "introduce-cache-config";

    /**
     * The set of currently introduced cache configuration file uris.
     * (we keep track of this so we don't re-introduce previously introduced urs).
     */
    private HashSet<String> m_setIntroducedURIs;

    /**
     * The prefix that was used to register the namespace.
     */
    private String m_sPrefix;

    /**
     * The {@link QualifiedName} representing the "introduce-cache-config" attribute.
     */
    private QualifiedName m_qnIntroduceCacheConfig;


    /**
     * Constructs an {@link XmlPreprocessingNamespaceHandler}.
     */
    public MultiXMLPreprocessingNamespaceHandler()
    {
    	super();
    	
        // remember the uri's we've already introduced
        m_setIntroducedURIs = new HashSet<String>();

        // establish a document preprocessor for the namespace
        DocumentElementPreprocessor dpp = new DocumentElementPreprocessor();

        // register an element preprocessor for "introduce-cache-config" directives
        dpp.addElementPreprocessor(new IntroduceMultiCacheConfigPreprocessor());

        // register an element preprocessor for "replace-with-file" directives
        dpp.addElementPreprocessor(new ReplaceWithFilePreprocessor());

        setDocumentPreprocessor(dpp);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onStartNamespace(ProcessingContext context,
                                 XmlElement        element,
                                 String            prefix,
                                 URI               uri)
    {
        super.onStartNamespace(context, element, prefix, uri);

        // we need to remember the prefix that was used to declare the namespace
        // so that we can use it later when pre-processing
        m_sPrefix = prefix;

        // establish some constants for the elements/attributes for this namespace
        m_qnIntroduceCacheConfig = new QualifiedName(m_sPrefix, INTRODUCE_CACHE_CONFIG);
    }


    /**
     * Provides support for custom pre-processing for foreign/non-Coherence-based
     * namespaces (ie: {@link NamespaceHandler}s) when using the 'introduce-cache-config'
     * pre-processor.
     */
    public interface IntroduceCacheConfigSupport
    {
        /**
         * Merges the non-Coherence-based element from one cache config into another
         * cache config (mutating the "into" cache config).
         *
         * @param sFromURI            the URI to which the from element belongs
         * @param xmlElement          the non-Coherence-based element from which
         *                            the merge should occur
         * @param xmlIntoCacheConfig  the cache config into which the merge should occur
         * @param qnOriginatedFrom    the QualifiedName to use for decorating
         *                            the element with the 'originated-from' attribute
         */
        void mergeConfiguration(ProcessingContext context,
                                String            sFromURI,
                                XmlElement        xmlElement,
                                XmlElement        xmlIntoCacheConfig,
                                QualifiedName     qnOriginatedFrom);
    }


    /**
     * The {@link IntroduceMultiCacheConfigPreprocessor} is an {@link ElementProcessor}
     * that introduces and merges one or more cache config resources/files
     * specified by the "introduce-cache-config" attribute in a <cache-config> element.
     */
    public class IntroduceMultiCacheConfigPreprocessor implements ElementPreprocessor
    {
        /**
         * The 'cache-config' {@link XmlElement}.
         */
        private static final String CACHE_CONFIG = "cache-config";


        /**
         * {@inheritDoc}
         */
        @Override
        public boolean preprocess(ProcessingContext context,
                                  XmlElement        xmlElement) throws ConfigurationException
        {
            // "introduce-cache-config" may only be applied with in a cache-config
            QualifiedName qName = xmlElement.getQualifiedName();

            if (!qName.hasPrefix() && qName.getLocalName().equals(CACHE_CONFIG))
            {
                // obtain the cache config URIs (a comma separated list in
                // the "introduce-cache-config" attribute)
                XmlValue xmlValue = xmlElement.getAttribute(m_qnIntroduceCacheConfig.getName());

                if (xmlValue == null)
                {
                    // nothing to do... as the directive isn't specified
                    return false;
                }
                else
                {
                    // get and then remove the "introduce-cache-config" attribute
                    // (so we don't attempt to merge it again)
                    String sCacheConfigURIs = xmlValue.getString();

                    xmlElement.getAttributeMap().remove(m_qnIntroduceCacheConfig.getName());

                    // pre-process (merge) the specified URIs into the current element
                    String[] arrURIs = sCacheConfigURIs.split(",");

                    // assume we haven't changed the configuration Xml
                    boolean fChangedXml = false;

                    for (String sURI : arrURIs)
                    {
                        sURI = sURI == null ? "" : sURI.trim();

                        if (!sURI.isEmpty())
                        {
                            // as we allow the URI to be an expression,
                            // we need to parse it first
                            ExpressionParser   parser = context.getExpressionParser();
                            Expression<String> exprURI;

                            try
                            {
                                exprURI = parser.parse(sURI, String.class);
                            }
                            catch (ParseException e)
                            {
                                throw new ConfigurationException("Failed to parse the 'introduce-cache-config' expression ["
                                                                 + sURI + "]",
                                                                 "Please ensure that the URI is correctly formatted",
                                                                 e);
                            }
                            
                            // now evaluate the expression to get the real URI!
                            sURI = exprURI.evaluate(context.getDefaultParameterResolver());

                            // CAV La URI puede contener varias URI
                            String[] arrConfURIs = sURI.split(",");

                            for (String sConfURI : arrConfURIs)
                            {
                                sURI = sConfURI == null ? "" : sConfURI.trim();

                                if (!sURI.isEmpty())
                                {
		                            // only process the URI if we've not already processed it.
		                            if (!m_setIntroducedURIs.contains(sURI))
		                            {
		                                // attempt to load the specified URI
		                                XmlElement xmlOtherElement = XmlHelper.loadFileOrResource(sURI,
		                                                                                          INTRODUCE_CACHE_CONFIG,
		                                                                                          context.getContextClassLoader());
		
		                                // ensure that the root element and this element are the same type
		                                if (xmlOtherElement == null)
		                                {
		                                    throw new ConfigurationException("Failed to load the 'introduce-cache-config' resource ["
		                                                                     + sURI + "]",
		                                                                     "Please ensure that is it available and has suitable permissions");
		                                }
		                                else
		                                {
		                                    if (xmlElement.getName().equals(xmlOtherElement.getName()))
		                                    {
		                                        // remember this URI as we want to avoid recursive pre-processing/introduction of it
		                                        m_setIntroducedURIs.add(sURI);
		
		                                        // merge the otherElement into the this element
		                                        mergeCacheConfig(context, sURI, xmlOtherElement, xmlElement);
		
		                                        // as we've merged, it's highly likely we changed the document,
		                                        // we have to let Coherence know the document
		                                        // should be considered for pre-processing again
		                                        fChangedXml = true;
		                                    }
		                                    else
		                                    {
		                                        throw new ConfigurationException("The 'introduce-cache-config' resource ["
		                                                                         + sURI + "] is not a <cache-config>",
		                                                                         "The root element of the resource must be a <cache-config>");
		                                    }
		                                }
		                            }
                                }
                            }
                        }
                    }

                    // let Coherence know if we've changed the configuration
                    return fChangedXml;
                }
            }
            else
            {
                // no change has been made
                return false;
            }
        }
    }
}
