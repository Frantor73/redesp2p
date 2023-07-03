import java.io.Serializable;
import java.util.List;

public class Request implements Serializable {
    private RequestType requestType;
    private String fileName;
    private List<String> fileList;

    public Request(RequestType requestType) {
        this.requestType = requestType;
    }

    public Request(RequestType requestType, String fileName) {
        this.requestType = requestType;
        this.fileName = fileName;
    }

    public Request(RequestType requestType, List<String> fileList) {
        this.requestType = requestType;
        this.fileList = fileList;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getFileName() {
        return fileName;
    }

    public List<String> getFileList() {
        return fileList;
    }
}
