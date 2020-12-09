package red.honey.oss.api.entiy;

import net.coobird.thumbnailator.geometry.Positions;

import java.io.File;
import java.io.Serializable;

/**
 * @author yangzhijie
 * @date 2020/8/21 10:18
 */
public class WaterMark implements Serializable {

    private static final long serialVersionUID = -1043007892597211502L;
    /**
     * 水印位置
     */
    private Positions positions;
    /**
     * 水印透明度
     */
    private float transparency;
    /**
     * 缩略图来源
     */
    private File waterMarkSource;

    public Positions getPositions() {
        return positions;
    }

    public void setPositions(Positions positions) {
        this.positions = positions;
    }

    public File getWaterMarkSource() {
        return waterMarkSource;
    }

    public void setWaterMarkSource(File waterMarkSource) {
        this.waterMarkSource = waterMarkSource;
    }

    public float getTransparency() {
        return transparency;
    }

    public void setTransparency(float transparency) {
        this.transparency = transparency;
    }
}
