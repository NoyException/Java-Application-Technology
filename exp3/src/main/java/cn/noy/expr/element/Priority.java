package cn.noy.expr.element;

public record Priority(double priority, Associativity associativity) implements Comparable<Priority>{
    @Override
    public int compareTo(Priority o) {
        if(this.priority != o.priority) return Double.compare(this.priority, o.priority);
        if(this.associativity != o.associativity) return this.associativity == Associativity.LEFT_TO_RIGHT ? -1 : 1;
        return 0;
    }
}
