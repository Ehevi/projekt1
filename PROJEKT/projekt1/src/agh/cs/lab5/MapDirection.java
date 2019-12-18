package agh.cs.lab5;

public enum MapDirection
{
    NORTH,
    SOUTH,
    EAST,
    WEST;

    public String toString()
    {
        if(this == NORTH) return "północ";
        else if(this == SOUTH) return "południe";
        else if(this == EAST) return "wschód";
        else return "zachód";
    }

    public MapDirection next()
    {
        if(this == NORTH) return EAST;
        else if(this == WEST) return NORTH;
        else if(this == SOUTH) return WEST;
        else return SOUTH;
    }

    public MapDirection previous()
    {
        if(this == NORTH) return WEST;
        else if(this == WEST) return SOUTH;
        else if(this == SOUTH) return EAST;
        else return NORTH;
    }

    public Vector2d toUnitVector()
    {
        if(this == NORTH) return new Vector2d(0, 1);
        else if(this == WEST) return new Vector2d(-1, 0);
        else if(this == SOUTH) return new Vector2d(0, -1);
        else return new Vector2d(1, 0);
    }
}