import java.io.FileInputStream;
import java.io.IOException;

public class FileUploader {
    public static void main(String[] args) throws IOException {
        String filePath = "zaheer.pptx";
        byte[] fileBytes = readFile(filePath);
        String md5Hash = calculateMd5Hash(fileBytes);
        System.out.println("MD5 hash of " + filePath + ": " + md5Hash);
    }

    private static byte[] readFile(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        byte[] fileBytes = new byte[fis.available()];
        fis.read(fileBytes);
        fis.close();
        return fileBytes;
    }

    private static String calculateMd5Hash(byte[] fileBytes) {
        int[] s = {
            7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
            5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20,
            4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
            6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
        };
        int[] k = new int[64];
        for (int i = 0; i < 64; i++) {
            k[i] = (int) (Math.pow(2, 32) * Math.abs(Math.sin(i + 1)));
        }
        int a0 = 0x67452301;
        int b0 = 0xEFCDAB89;
        int c0 = 0x98BADCFE;
        int d0 = 0x10325476;
        int[] words = new int[16];
        int a = a0;
        int b = b0;
        int c = c0;
        int d = d0;
        for (int i = 0; i < fileBytes.length; i += 64) {
            for (int j = 0; j < 16; j++) {
                int word = 0;
                for (int l = 0; l < 4; l++) {
                    int byteIndex = i + j * 4 + l;
                    if (byteIndex < fileBytes.length) {
                        word |= (fileBytes[byteIndex] & 0xFF) << (l * 8);
                    }
                }
                words[j] = word;
            }
            int aa = a;
            int bb = b;
            int cc = c;
            int dd = d;
            for (int j = 0; j < 64; j++) {
                int f, g;
                if (j < 16) {
                    f = (b & c) | (~b & d);
                    g = j;
                } else if (j < 32) {
                    f = (d & b) | (~d & c);
                    g = (5 * j + 1) % 16;
                } else if (j < 48) {
                    f = b ^ c ^ d;
                    g = (3 * j + 5) % 16;
                } else {
                    f = c ^ (b | ~d);
                    g = (7 * j) % 16;
                }
                int temp = d;
                d = c;
                c = b;
                b = b + Integer.rotateLeft(a + f + k[j] + words[g], s[j]);
                a = temp;
            }
            a0 += a;
            b0 += b;
            c0 += c;
            d0 += d;
        }
        byte[] md5HashBytes = new byte[16];
        int index = 0;
        for (int i = 0; i < 4; i++) {
            int value = (i == 0) ? a0 : (i == 1) ? b0 : (i == 2) ? c0 : d0;
            for (int j = 0; j < 4; j++) {
                md5HashBytes[index++] = (byte) (value & 0xFF);
                value >>>= 8;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (byte b1 : md5HashBytes) {
            sb.append(Integer.toHexString((b1 & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }
}
