import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HoffmanDecoder {

    private Hoffman hoffmanCode;
    private File compressedFile;
    private byte[] textCode;

    public HoffmanDecoder(File compressedFile) throws IOException {
        this.compressedFile = compressedFile;
        Path path = Paths.get(compressedFile.getName());
        try {
            textCode = Files.readAllBytes(path);
        } catch (java.lang.OutOfMemoryError ignored) {
        }
    }

    public void hoffmanFileDecompression() throws IOException, QueueException, HugeFileException {
        if (textCode == null) throw new HugeFileException();

        int[] twoPower = {1, 2, 4, 8, 16, 32, 64, 128};
        char[] text = new char[200000000];
        int charCounter = 0;

        boolean[] code = new boolean[128];
        int codeCounter = 0;

        File decompressedFile = new File("DecompressedFile.txt");
        PrintWriter writer = new PrintWriter(decompressedFile);

        InputStream inFileStream = new FileInputStream(compressedFile);
        DataInputStream inputStream = new DataInputStream(inFileStream);

        int size = keyProcessor(inputStream);
        byte idleBit = hoffmanCode.getIdleBit();

        for (int i = size; i < textCode.length; i++) {
            byte readByte = textCode[i];
            int number = 0;
            if (i == textCode.length - 1) {
                number = idleBit;
            }

            for (int j = number; j < 8; j++) {
                byte bitNum = (byte) (readByte & twoPower[7 - j]);

                if (bitNum == 0) {
                    code[codeCounter] = false;
                    codeCounter++;
                } else {
                    code[codeCounter] = true;
                    codeCounter++;
                }

                char correspond = hoffmanCode.getHoffmanKey(code, codeCounter);

                if (correspond != '\000') {
                    text[charCounter] = correspond;
                    charCounter++;
                    codeCounter = 0;
                    if (charCounter == 200000000) {
                        writer.print(text);
                        text = new char[200000000];
                        charCounter = 0;
                    }
                }
            }
        }


        char[] cloneText = new char[charCounter + 1];
        System.arraycopy(text, 0, cloneText, 0, charCounter + 1);
        writer.print(cloneText);

        writer.close();
        inFileStream.close();
        inputStream.close();
    }

    public void hugeFileDecompression() throws IOException, QueueException {
        int[] twoPower = {1, 2, 4, 8, 16, 32, 64, 128};
        char[] text = new char[200000000];
        int charCounter = 0;

        boolean[] code = new boolean[128];
        int codeCounter = 0;

        File decompressedFile = new File("DecompressedFile.txt");
        PrintWriter writer = new PrintWriter(decompressedFile);

        InputStream inFileStream = new FileInputStream(compressedFile);
        DataInputStream inputStream = new DataInputStream(inFileStream);

        int size = keyProcessor(inputStream);
        byte idleBit = hoffmanCode.getIdleBit();
        long fileSize = compressedFile.length();

        while (fileSize > 0) {
            if (fileSize > 100000000) {
                textCode = new byte[100000000];
                fileSize = fileSize - 100000000;
            } else {
                textCode = new byte[Math.toIntExact(fileSize)];
                fileSize = 0;
            }
            inputStream.read(textCode);

            for (int i = size; i < textCode.length; i++) {
                byte readByte = textCode[i];
                int number = 0;
                if (fileSize == 0 && i == textCode.length - 1) {
                    number = idleBit;
                }

                for (int j = number; j < 8; j++) {
                    byte bitNum = (byte) (readByte & twoPower[7 - j]);

                    if (bitNum == 0) {
                        code[codeCounter] = false;
                        codeCounter++;
                    } else {
                        code[codeCounter] = true;
                        codeCounter++;
                    }

                    char correspond = hoffmanCode.getHoffmanKey(code, codeCounter);

                    if (correspond != '\000') {
                        text[charCounter] = correspond;
                        charCounter++;
                        codeCounter = 0;
                        if (charCounter == 200000000) {
                            writer.print(text);
                            text = new char[200000000];
                            charCounter = 0;
                        }
                    }
                }
            }
            size = 0;
        }

        char[] cloneText = new char[charCounter + 1];
        System.arraycopy(text, 0, cloneText, 0, charCounter + 1);
        writer.print(cloneText);

        writer.close();
        inFileStream.close();
        inputStream.close();
    }

    private int keyProcessor(DataInputStream inputStream) throws IOException, QueueException {
        Map<Integer> numberMap = new Map<>();
        int size = 0;

        while (true) {
            char key = inputStream.readChar();
            size += 2;
            if (key == '\000') {
                break;
            }
            int repetition = inputStream.readInt();
            size += 4;
            numberMap.put(key, repetition);
        }
        hoffmanCode = new Hoffman(numberMap);
        return size;
    }
}
