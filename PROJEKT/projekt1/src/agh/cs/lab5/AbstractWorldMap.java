package agh.cs.lab5;


abstract public class AbstractWorldMap implements IWorldMap
{
    protected MapVisualizer mapVisualizer = new MapVisualizer(this);

    public abstract Vector2d getUpperRight();
    public abstract Vector2d getLowerLeft();

    @Override
    public boolean isOccupied(Vector2d position)
    {
        return (objectAt(position) != null);
    }

    @Override
    public String toString()
    {
        return mapVisualizer.draw(getLowerLeft(), getUpperRight());
    }
}