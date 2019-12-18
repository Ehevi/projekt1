package test;

import agh.cs.lab5.*;
import org.junit.*;
import java.lang.reflect.Array;

public class RectangularMapTest
{
    AbstractWorldMap map;
    Animal animal1;
    Animal animal2;
    OptionsParser optionsParser = new OptionsParser();

    @Before
    public void setUp() throws Exception
    {
        map = new RectangularMap(10,10);
        animal1 = new Animal(map); //domyslnie dostanie wektor (2, 2)
        animal2 = new Animal(map, new Vector2d(3,4));
        map.place(animal1);
        map.place(animal2);
    }

    @Test
    public void testObjectAt()
    {
        Assert.assertNotNull(map.objectAt(new Vector2d(2,2)));
        Assert.assertNull(map.objectAt(new Vector2d(2,3)));
        Assert.assertNull(map.objectAt(new Vector2d(0,0)));
        Assert.assertSame(map.objectAt(new Vector2d(2,2)), animal1);
        Assert.assertSame(map.objectAt(new Vector2d(3,4)), animal2);
    }

    @Test
    public void testIsOccupied()
    {
        Assert.assertTrue(map.isOccupied(new Vector2d(3,4)));
        Assert.assertTrue(map.isOccupied(new Vector2d(2,2 )));
        Assert.assertFalse(map.isOccupied(new Vector2d(10,5)));
        Assert.assertFalse(map.isOccupied(new Vector2d(4, 4)));
    }

    @Test
    public void testCanMoveTo()
    {
        Assert.assertFalse(1==2);
        Assert.assertTrue(map.canMoveTo(new Vector2d(0,0))); //ok
        Assert.assertTrue(map.canMoveTo(new Vector2d(4,4))); //ok
        Assert.assertFalse(map.canMoveTo(new Vector2d(2,2)));
        Assert.assertFalse(map.canMoveTo(new Vector2d(3,4)));
        Assert.assertFalse(map.canMoveTo(new Vector2d(11,4)));
        Assert.assertFalse(map.canMoveTo(new Vector2d(10,-1)));
        Assert.assertFalse(map.canMoveTo(new Vector2d(10,6)));
    }

    @Test()
    public void testPlace()
    {
        Assert.assertTrue(map.place(new Animal(map, new Vector2d(0,0))));
        Assert.assertTrue(map.place(new Animal(map, new Vector2d(4,4))));
        Assert.assertFalse(map.place(new Animal(map, new Vector2d(0,-1))));
        Assert.assertFalse(map.place(new Animal(map, new Vector2d(2,2))));
    }

}