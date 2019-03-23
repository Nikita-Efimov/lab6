import java.util.Arrays;
import java.io.Serializable;

abstract class Place implements Serializable {
    protected String name = "Без имени";

    public Place(String string) {
        name = string;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object == null)
            return false;

        if (this.getClass() != object.getClass())
            return false;

        Place place = (Place)object;
        if (this.name != place.name)
            return false;

        return true;
    }

    @Override
	public int hashCode() {
		return Arrays.deepHashCode((Object[])new Object[]{name});
	}
}
