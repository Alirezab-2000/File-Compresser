import java.io.*;


public class HoffmanEncoder {

    private File uncompressed_file;
    private String expression;
    private Hoffman hoffmanCode;
    char content[];


    public HoffmanEncoder(File uncompressed_file) throws IOException, QueueException {
        this.uncompressed_file = uncompressed_file;

        int size = Math.toIntExact(uncompressed_file.length());
        try {
            FileReader unComFileReader = new FileReader(uncompressed_file);
            content = new char[size];
            unComFileReader.read(content);
            Map<Integer> numberMap = repeatMappers();
            hoffmanCode = new Hoffman(numberMap);
        } catch (java.lang.OutOfMemoryError ignored) {
        }
    }

    public HoffmanEncoder(String exp) throws IOException, QueueException {
        this.expression = exp;

        content = expression.toCharArray();
        Map<Integer> numberMap = repeatMappers();
        hoffmanCode = new Hoffman(numberMap);
    }

    private Map<Integer> repeatMappers() throws IOException {
        // this method is used to iterate over a character array that given to it for mapping it items
        Map<Integer> numberMap = new Map<>();
        for (char character : content) {
            if (character == '\000') {
                continue;
            }
            Integer value = numberMap.getValue(character);
            if (value != null) {
                // if an item doesnt exist it maps to 1
                numberMap.put(character, value + 1);
            } else {
                // if an item exist it maps to  (last value + 1)
                numberMap.put(character, 1);
            }
        }
        return numberMap;
    }

    public void compressionFile() throws IOException, QueueException, HugeFileException {
        if(content == null) throw new HugeFileException();

        byte[] textCode = new byte[Short.MAX_VALUE];
        int byte_counter = 0;
        int bit_counter = 0;

        File compressed_file = new File("compressed.comd");
        OutputStream fileStream = new FileOutputStream(compressed_file);
        DataOutputStream outputStream = new DataOutputStream(fileStream);

        decodingKeyWriter(outputStream);

        for (char character : content) {
            if (character == '\000') continue;
            boolean[] code = hoffmanCode.getHoffmanCode(character);
            for (boolean bit : code) {
                if (bit) {
                    textCode[byte_counter] = (byte) (textCode[byte_counter] | 1);
                }
                if (bit_counter == 7) {
                    byte_counter++;
                    bit_counter = 0;

                    if (byte_counter == Short.MAX_VALUE) {
                        outputStream.write(textCode);
                        textCode = new byte[Short.MAX_VALUE];
                        byte_counter = 0;
                    }
                } else {
                    textCode[byte_counter] = (byte) (textCode[byte_counter] << 1);
                    bit_counter++;
                }
            }
        }
        textCode[byte_counter] = (byte) (textCode[byte_counter] >> 1);
        if (textCode[byte_counter] == 0 && bit_counter == 0) {
            byte_counter--;
        }

        byte[] cloneTextCode = new byte[byte_counter + 1];
        System.arraycopy(textCode, 0, cloneTextCode, 0, byte_counter + 1);
        outputStream.write(cloneTextCode);
        fileStream.close();
        outputStream.close();
    }

    private void hugeFileRepeatMappers() throws IOException, QueueException {
        Map<Integer> numberMap = new Map<>();
        FileReader unComFileReader = new FileReader(uncompressed_file);

        for (int i = 0; i <= (uncompressed_file.length() / 350000000); i++) {
            content = new char[100000000];
            unComFileReader.read(content);
            for (char character : content) {
                if (character == '\000') {
                    continue;
                }
                Integer value = numberMap.getValue(character);
                if (value != null) {
                    // if an item doesnt exist it maps to 1
                    numberMap.put(character, value + 1);
                } else {
                    // if an item exist it maps to  (last value + 1)
                    numberMap.put(character, 1);
                }
            }
        }
        hoffmanCode = new Hoffman(numberMap);
    }

    public void hugeFileCompression() throws IOException, QueueException {
        hugeFileRepeatMappers();
        byte[] textCode = new byte[Short.MAX_VALUE];
        int byte_counter = 0;
        int bit_counter = 0;

        File compressed_file = new File("compressed.comd");
        OutputStream fileStream = new FileOutputStream(compressed_file);
        DataOutputStream outputStream = new DataOutputStream(fileStream);

        decodingKeyWriter(outputStream);
        for (int i = 0; i <= (uncompressed_file.length() / 100000000); i++) {
            content = new char[100000000];
            FileReader unComFileReader = new FileReader(uncompressed_file);
            unComFileReader.read(content);
            for (char character : content) {
                if (character == '\000') continue;
                boolean[] code = hoffmanCode.getHoffmanCode(character);
                for (boolean bit : code) {
                    if (bit) {
                        textCode[byte_counter] = (byte) (textCode[byte_counter] | 1);
                    }
                    if (bit_counter == 7) {
                        byte_counter++;
                        bit_counter = 0;

                        if (byte_counter == Short.MAX_VALUE) {
                            outputStream.write(textCode);
                            textCode = new byte[Short.MAX_VALUE];
                            byte_counter = 0;
                        }
                    } else {
                        textCode[byte_counter] = (byte) (textCode[byte_counter] << 1);
                        bit_counter++;
                    }
                }
            }
        }

        textCode[byte_counter] = (byte) (textCode[byte_counter] >> 1);
        if (textCode[byte_counter] == 0 && bit_counter == 0) {
            byte_counter--;
        }

        byte[] cloneTextCode = new byte[byte_counter + 1];
        System.arraycopy(textCode, 0, cloneTextCode, 0, byte_counter + 1);
        outputStream.write(cloneTextCode);
        fileStream.close();
        outputStream.close();
    }

    public void printHoffman() {
        System.out.print(hoffmanCode);
    }

    // helping method //////////////////////////////////////////////////////
    private void decodingKeyWriter(DataOutputStream outStream) throws IOException {
        char[] keys = hoffmanCode.getAllKeys();

        for (char key : keys) {
            int keySize = hoffmanCode.getRepetitionNumber(key);

            outStream.writeChar(key);
            outStream.writeInt(keySize);
        }
        char endIdentifier = '\000';
        outStream.writeChar(endIdentifier);
    }

}
