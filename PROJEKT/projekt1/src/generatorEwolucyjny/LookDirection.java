package generatorEwolucyjny;

import agh.cs.lab5.Vector2d;

public enum LookDirection {
    N, NE, E, SE, S, SW, W, NW;

    public LookDirection next()
    {
        if(this == N) return NE;
        else if(this == NE) return E;
        else if(this == E) return SE;
        else if(this == SE) return S;
        else if(this == S) return SW;
        else if(this == SW) return W;
        else if(this == W) return NW;
        else return N;//nw
    }

    public LookDirection previous()
    {
        if(this == N) return NW;
        else if(this == NE) return N;
        else if(this == E) return NE;
        else if(this == SE) return E;
        else if(this == S) return SE;
        else if(this == SW) return S;
        else if(this == W) return SW;
        else return W;//nw
    }

    public Vector2d toUnitVector()
    {
        if(this == N) return new Vector2d(0, 1);
        else if(this == NE) return new Vector2d(1, 1);
        else if(this == E) return new Vector2d(1, 0);
        else if(this == SE) return new Vector2d(1, -1);
        else if(this == S) return new Vector2d(0, -1);
        else if(this == SW) return new Vector2d(-1, -1);
        else if(this == W) return new Vector2d(-1, 0);
        else return new Vector2d(-1, 1);//nw
    }
}