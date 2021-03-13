package cloud.souris.aytos.mc.safari.areas;

import java.util.Objects;

public class AreaFlag {
    private String name;
    private boolean allowed;

    public AreaFlag(String name, boolean allowed) {
        this.name = name;
        this.allowed = allowed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AreaFlag areaFlag = (AreaFlag) o;
        return isAllowed() == areaFlag.isAllowed() && Objects.equals(getName(), areaFlag.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), isAllowed());
    }

    @Override
    public String toString() {
        return name + ": " + (allowed ? "allowed" : "disabled");
    }
}
