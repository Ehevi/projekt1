package agh.cs.lab5;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OptionsParser
{
    private Map<String, MoveDirection> dictionary = new HashMap<>();

    public OptionsParser() //zamiana lancuchow znakow na KIERUNEK: zdef. jako slownik
    {
        dictionary.put("f", MoveDirection.FORWARD);
        dictionary.put("forward", MoveDirection.FORWARD);

        dictionary.put("b", MoveDirection.BACKWARD);
        dictionary.put("backward", MoveDirection.BACKWARD);

        dictionary.put("r", MoveDirection.RIGHT);
        dictionary.put("right", MoveDirection.RIGHT);

        dictionary.put("l", MoveDirection.LEFT);
        dictionary.put("left", MoveDirection.LEFT);
    }

    public MoveDirection[] parse(String[] commands) //zwraca tablice kierunkow ruchu
    {
        List<MoveDirection> directions = new LinkedList<>();
        for(String command : commands)
        {
            if(dictionary.containsKey(command))
            { //jezeli w slowniku jest zdefiniowany kierunek, to dokladam go do linklisty
                directions.add(dictionary.get(command));
            }
            //else: jezeli kierunek jest nieznany: pomijamy go
        }
        return directions.stream().toArray(MoveDirection[]::new);
        //zwracam linkliste w postaci tablicy
    }
}