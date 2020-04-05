package tools;

public class Calculator {

  private static int[] transformMask(int mask) {
    if (mask <= 0 || mask >= 32) {
      int[] result = {255, 255, 255, 255};
      return result;
    }
    int[] maskBytes = new int[4];
    int n = mask / 8;
    for (int i = 0; i < n; i++) {
      maskBytes[i] = 255;
    }
    if ((mask % 8) != 0) {
      maskBytes[n] = 256 - (1 << (8 - (mask % 8)));
    }
    return maskBytes;
  }

  public static int[] getWildcard(int mask) {
    int[] wildcard = new int[4];
    if (mask <= 0 || mask >= 32) return wildcard;
    int n = mask / 8;
    for (int i = 0; i < n; i++) {
      wildcard[i] = 0;
    }
    if ((mask % 8) != 0) wildcard[n] = (1 << (8 - (mask % 8))) - 1;
    else wildcard[n] = 255;
    for (int i = n + 1; i < 4; i++) {
      wildcard[i] = 255;
    }
    return wildcard;
  }

  public static int[] getNetAddress(int[] host, int mask) {
    int[] netAddress = new int[4];
    int[] maskBytes = transformMask(mask);
    for (int i = 0; i < 4; i++) {
      netAddress[i] = host[i] & maskBytes[i];
    }
    return netAddress;
  }

  public static int[] getHostMin(int[] host, int mask) {
    int[] netAddress = getNetAddress(host, mask);
    netAddress[3] += 1;
    return netAddress;
  }

  public static int[] getBroadcast(int[] host, int mask) {
    int[] netAddress = getNetAddress(host, mask);
    int[] wildcard = getWildcard(mask);
    int[] broadcast = new int[4];
    for (int i = 0; i < 4; i++) {
      broadcast[i] = netAddress[i] | wildcard[i];
    }
    return broadcast;
  }

  public static int[] getHostMax(int[] host, int mask) {
    int[] broadcast = getBroadcast(host, mask);
    broadcast[3] -= 1;
    return broadcast;
  }

  public static int getHostsNumber(int mask) {
    return (int) Math.pow(2, (32 - mask)) - 2;
  }

  public static char getIpClass(int[] host) {
    if (host[0] < 128) return 'A';
    if (host[0] >= 128 && host[0] < 192) return 'B';
    if (host[0] >= 192 && host[0] < 224) return 'C';
    if (host[0] >= 224 && host[0] < 240) return 'D';
    else return 'E';
  }

  public static void main(String[] args) {
    int[] host = {37, 55, 121, 118};
    int[] result = getNetAddress(host, 18);
    for (int i = 0; i < result.length; i++) {
      System.out.print(result[i] +  ".");
    }
    System.out.println();
    System.out.println(getHostsNumber(18));
    System.out.println(getIpClass(host));
  }
}
