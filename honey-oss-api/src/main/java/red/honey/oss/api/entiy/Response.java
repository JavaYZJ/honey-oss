package red.honey.oss.api.entiy;

import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/11/11 16:13
 */
public class Response<T> {

    private List<T> success;

    private List<T> failures;

    private String fileKey;

    public List<T> getSuccess() {
        return success;
    }

    public void setSuccess(List<T> success) {
        this.success = success;
    }

    public List<T> getFailures() {
        return failures;
    }

    public void setFailures(List<T> failures) {
        this.failures = failures;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }
}
