import java.util.Arrays;
import java.util.Date;

class City extends Place implements Comparable {
	protected Integer areaSize, x, y;
	protected Date initDate;

	public City(String name, Integer areaSize) {
		this(name, areaSize, 0, 0);
	}

	public City(String name, Integer areaSize, Integer x, Integer y) {
		super(name);
		initDate = new Date();
		this.areaSize = areaSize;
		this.x = x;
		this.y = y;
	}

	@Override
	public int compareTo(Object object) {
		if (this == object)
            return 0;

		if (object == null)
			throw new NullPointerException();

		if (this.getClass() != object.getClass())
			throw new ClassCastException();

		City city = (City)object;
		return this.areaSize.compareTo(city.areaSize) + this.name.compareTo(city.name);
	}

	@Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object == null)
            return false;

        if (this.getClass() != object.getClass())
            return false;

        City city = (City)object;
        if (!this.name.equals(city.name) || !this.areaSize.equals(city.areaSize))
            return false;

        return true;
    }

    @Override
	public int hashCode() {
		return Arrays.deepHashCode((Object[])new Object[]{name, areaSize});
	}

    @Override
    public String toString() {
        return "Имя города: " + name + "\nПлощадь: " + areaSize + "\nКоординаты: " + x + ", " + y + "\nДата создания объекта: " + (String)initDate.toLocaleString();
    }
}
