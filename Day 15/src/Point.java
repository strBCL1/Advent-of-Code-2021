import java.util.Comparator;
import java.util.Objects;

class Point implements Comparable<Point> {
	int riskLevel;
	int y, x;
	
	double g, h, f;
	int parentY, parentX;
	
	Point(int riskLevel, int y, int x) {
		this.riskLevel = riskLevel;
		this.y = y;
		this.x = x;
	}
	
	Point(Point p) {
		this.riskLevel = p.riskLevel;
		this.y = p.y;
		this.x = p.x;
		this.f = p.f;
		this.h = p.h;
		this.g = p.g;
		this.parentY = p.parentY;
		this.parentX = p.parentX;
	}

	@Override
	public int hashCode() {
		return Objects.hash(f, g, h, riskLevel, x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
//		return Double.doubleToLongBits(f) <= Double.doubleToLongBits(other.f)
//				&& Double.doubleToLongBits(g) <= Double.doubleToLongBits(other.g)
//				&& Double.doubleToLongBits(h) <= Double.doubleToLongBits(other.h)
//				&& riskLevel <= other.riskLevel && x == other.x && y == other.y;
		
		return Double.doubleToLongBits(f) == Double.doubleToLongBits(other.f)
				&& Double.doubleToLongBits(g) == Double.doubleToLongBits(other.g)
				&& Double.doubleToLongBits(h) == Double.doubleToLongBits(other.h)
				&& riskLevel == other.riskLevel && x == other.x && y == other.y;
	}
	
	public int compareTo(Point o) {
		return Comparator.comparing(Point::getF)
				.thenComparing(Point::getH)
				.compare(this, o);
	}

	@Override
	public String toString() {
		return "Point [riskLevel=" + riskLevel + ", y=" + y + ", x=" + x + ", g=" + g + ", h=" + h + ", f=" + f
				+ ", parentY=" + parentY + ", parentX=" + parentX + "]";
	}

	public int getRiskLevel() {
		return riskLevel;
	}

	public double getG() {
		return g;
	}

	public double getH() {
		return h;
	}

	public double getF() {
		return f;
	}
}
