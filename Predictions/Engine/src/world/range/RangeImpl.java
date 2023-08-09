package world.range;

public class RangeImpl {
    private final Float from;
    private final  Float to;
    public RangeImpl(Float from, Float to) {
        this.from = from;
        this.to = to;
    }

    public Float getFrom() {
        return from;
    }

    public Float getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "from=" + from +
                ", to=" + to ;
    }

}
