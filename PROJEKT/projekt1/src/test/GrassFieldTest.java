package test;

import agh.cs.lab5.*;
import org.junit.*;
import java.lang.reflect.Array;

public class GrassFieldTest
{
    AbstractWorldMap map;
    Animal animal1;
    Animal animal2;
    OptionsParser optionsParser = new OptionsParser();

    @Before
    public void setUp() throws Exception
    {
        map = new GrassField(0);
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
        Assert.assertTrue(map.canMoveTo(new Vector2d(0,0)));
        Assert.assertTrue(map.canMoveTo(new Vector2d(4,4)));
        Assert.assertFalse(map.canMoveTo(new Vector2d(2,2)));
        Assert.assertFalse(map.canMoveTo(new Vector2d(3,4)));
        Assert.assertTrue(map.canMoveTo(new Vector2d(11,4)));
        Assert.assertTrue(map.canMoveTo(new Vector2d(10,-1)));
        Assert.assertTrue(map.canMoveTo(new Vector2d(10,6)));
    }

    @Test()
    public void testPlace()
    {
        Assert.assertTrue(map.place(new Animal(map, new Vector2d(0,0))));
        Assert.assertTrue(map.place(new Animal(map, new Vector2d(4,4))));
        Assert.assertTrue(map.place(new Animal(map, new Vector2d(0,-1))));
    }

    @Test
    public void testMapUpdate() throws Exception
    {
        GrassField grassField = new GrassField(0);
        animal1 = new Animal(grassField, new Vector2d(3,1));
        animal2 = new Animal(grassField, new Vector2d(4,2));
        Animal animal3 = new Animal(grassField, new Vector2d(0,5));
        Animal animal4 = new Animal(grassField, new Vector2d(5,0));

        grassField.place(animal1);
        grassField.place(animal2);
        grassField.place(animal3);
        grassField.place(animal4);

        grassField.run(optionsParser.parse(new String[]{"l"}));

        Assert.assertEquals(new Vector2d(0,0), grassField.getLowerLeft());
        Assert.assertEquals(new Vector2d(5,5), grassField.getUpperRight());

        for(int i = 0; i < 10; i++)
        {
            grassField.run(optionsParser.parse(new String[]{"f","f"}));
        }

        Assert.assertEquals(new Vector2d(-7,0), grassField.getLowerLeft());
        Assert.assertEquals(new Vector2d(5,12), grassField.getUpperRight());

        for(int i = 0; i < 10; i++)
        {
            grassField.run(optionsParser.parse(new String[]{"b","b"}));
        }

        Assert.assertEquals(new Vector2d(0,0), grassField.getLowerLeft());
        Assert.assertEquals(new Vector2d(5,5), grassField.getUpperRight());

        for(int i = 0; i < 10; i++)
        {
            grassField.run(optionsParser.parse(new String[]{"b","b"}));
        }

        Assert.assertEquals(new Vector2d(0,-7), grassField.getLowerLeft());
        Assert.assertEquals(new Vector2d(13,5), grassField.getUpperRight());
    }
}