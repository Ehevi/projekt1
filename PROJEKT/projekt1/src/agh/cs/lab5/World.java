package agh.cs.lab5;

public class World
{
    public static void main(String[] args)
    {
        MoveDirection[] directions = new OptionsParser().parse(args);
        IWorldMap map = new GrassField(10);
        System.out.println(map);
        map.place(new Animal(map)); //czyli domyslnie wektor (2, 2)
        map.place(new Animal(map,new Vector2d(0,0)));
        map.place(new Animal(map,new Vector2d(9,11)));
        //mapUpdate pokazuje tylko wybrany obszar, na ktorym cos jest
        System.out.println(map);
        map.run(directions);
        System.out.println(map);
    }
}