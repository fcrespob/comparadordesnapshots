package es.mapfre.solvencia.coherence.cloud;

import java.net.InetAddress;
import java.util.Comparator;

public class CloudInetAddressComparator implements Comparator<InetAddress>
{
  public static final CloudInetAddressComparator INSTANCE = new CloudInetAddressComparator();

  public int compare(InetAddress addrA, InetAddress addrB)
  {
    if (addrA == addrB)
    {
      return 0;
    }
    if (addrA == null)
    {
      return -1;
    }
    if (addrB == null)
    {
      return 1;
    }
    if ((addrA.isLoopbackAddress()) && (addrB.isLoopbackAddress()))
    {
      return 0;
    }

    byte[] abA = addrA.getAddress();
    byte[] abB = addrB.getAddress();

    int cbA = abA.length;
    int ofA = 0; for (; (ofA < cbA) && (abA[ofA] == 0); ofA++);
    int cbsA = cbA - ofA;

    int cbB = abB.length;
    int ofB = 0; for (; (ofB < cbB) && (abB[ofB] == 0); ofB++);
    int cbsB = cbB - ofB;

    if (cbsA < cbsB)
    {
      return -1;
    }
    if (cbsB < cbsA)
    {
      return 1;
    }

    int i = 0;
    int c = cbsA;
    while ((i < c) && (abA[(ofA + i)] == abB[(ofB + i)])) i++;

    return ((i < c) && (0xFF & abA[i]) < (0xFF & abB[i])) ? -1 : i == c ? 0 : 1;
  }
}