package agh.cs.lab5;

import java.util.ArrayList;
import java.util.List;

public class RectangularMap extends AbstractWorldMap implements IWorldMap
{

    public final int width;
    public final int height;
    private List<Animal> animals = new ArrayList<>();

    public RectangularMap(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean canMoveTo(Vector2d position)
    {
        return position.precedes(new Vector2d(width-1, height-1)) && position.follows(new Vector2d(0, 0)) && !isOccupied(position);
    }

    @Override
    public boolean place(Animal animal)
    {
        if(canMoveTo(animal.getPosition()))
        {
            animals.add(animal);
            return true;
        }
        else {return false;}
    }

    @Override
    public void run(MoveDirection[] directions)
    {
        for(int i = 0; i < directions.length; i++)
        {
            animals.get(i%animals.size()).move(directions[i]);
        }
    }

    @Override
    public Object objectAt(Vector2d position)
    {
        return animals.stream().filter(animal -> position.equals(animal.getPosition())).findAny().orElse(null);
    }

    @Override
    public Vector2d getUpperRight() {
        return new Vector2d(width, height);
    }

    @Override
    public Vector2d getLowerLeft() {
        return new Vector2d(0,0);
    }

}