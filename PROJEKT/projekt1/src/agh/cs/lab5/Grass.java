package agh.cs.lab5;

public class Grass implements IMapElement
{
    private Vector2d position;

    public Grass(Vector2d  position) //konstruktor
    {this.position = position;}

    public Vector2d getPosition()//zwraca pozycje kepki trawy
    {return position;}

    @Override
    public String toString() //reprezentacja graficzna kepki trawy
    {return "*";}
}