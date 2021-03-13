package cloud.souris.aytos.mc.safari.areas;

import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;

public class AreaBounds {
    private final Vector3 center;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double minZ;
    private double maxZ;

    public AreaBounds(Vector3 center) {
        this.center = center.floor();
        setupBounds();
    }

    private void setupBounds() {
        this.minX = center.x - 150;
        this.maxX = center.x + 150;

        this.minY = center.y - 150;
        this.maxY = center.y + 150;

        this.minZ = center.z - 150;
        this.maxZ = center.z + 150;
    }

    public boolean checkCollision(Position check) {
        return check.x >= minX && check.x <= maxX
                && check.y >= minY && check.y <= maxY
                && check.z >= minZ && check.z <= maxZ;
    }

    public Vector3 getCenter() {
        return center;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMinZ() {
        return minZ;
    }

    public double getMaxZ() {
        return maxZ;
    }

    @Override
    public String toString() {
        return "  X:  " + getMinX() + " - " + getMaxX() + "\n" +
                "  Y:  " + getMinY() + " - " + getMaxY() + "\n" +
                "  Z:  " + getMinZ() + " - " + getMaxZ();
    }
}
