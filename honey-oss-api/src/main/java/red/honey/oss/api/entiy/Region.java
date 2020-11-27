package red.honey.oss.api.entiy;

import java.io.Serializable;

/**
 * @author yangzhijie
 * @date 2020/8/21 10:15
 */

public class Region implements Serializable {

    private static final long serialVersionUID = 8016320752201357647L;

    private int left;

    private int top;

    private int right;

    private int button;

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }
}
