package generatorEwolucyjny;

import agh.cs.lab5.*;

public class Plant
{
    public Vector2d position;
    public ProjectMap map;

    public Plant(ProjectMap map, Vector2d position)
    {
        this.position=position;
        this.map=map;
    }
}