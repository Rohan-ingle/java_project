
public abstract class FileOperator {
    protected String filePath;
    protected AESEncryptor encryptor;

    public FileOperator(String filePath, AESEncryptor encryptor) {
        this.filePath = filePath;
        this.encryptor = encryptor;
    }

    abstract void operateFile() throws Exception;
}
