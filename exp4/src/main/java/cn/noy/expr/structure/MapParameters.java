package cn.noy.expr.structure;

import cn.noy.expr.element.val.Value;

import java.util.HashMap;
import java.util.Map;

public class MapParameters implements Parameters{
    private final Map<String, ExpressionNode> map;

    public MapParameters(){
        map = new HashMap<>();
    }

    public MapParameters(Map<String, ?> map){
        this.map = new HashMap<>();
        map.forEach((k, v) -> {
            if(v instanceof ExpressionNode node)
                this.map.put(k, node);
            else if(v instanceof Value value)
                this.map.put(k, new NodeValue(value));
        });
    }
    
    @Override
    public ExpressionNode get(String name) {
        return map.get(name);
    }

    @Override
    public void set(String name, Value value) {
        set(name, new NodeValue(value));
    }

    @Override
    public void set(String name, ExpressionNode value) {
        map.put(name, value);
    }
    
    @Override
    public boolean contains(String name) {
        return map.containsKey(name);
    }
}
