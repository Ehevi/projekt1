package generatorEwolucyjny;

import agh.cs.lab5.*;

import java.util.ArrayList;
import java.util.Random;

public class ProjectMap {
    public int moveEnergy;
    public int energyFromPlant;
    public int startEnergy;
    private Vector2d lowerLeft; //cała mapa
    private Vector2d upperRight; //cała mapa
    public Vector2d jungleLowerLeft; //dżungla na środku
    public Vector2d jungleUpperRight; //dżungla na środku
    protected ArrayList<ProjectAnimal> animals;
    protected ArrayList<Plant> plants;
    protected MapVisualizer mapVisualizer = new MapVisualizer(this);

    public ProjectMap(int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int plantEnergy, int numberOfAnimals)
    {
        this.moveEnergy=moveEnergy;
        this.energyFromPlant=plantEnergy;
        this.startEnergy=startEnergy;
        width=width-1;
        height=height-1; //bo start od zera
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width, height);
        int x=(int)((width- Math.floor((jungleRatio * width)/(1+jungleRatio)))*0.5);
        int y=(int)((height-Math.floor((jungleRatio * height)/(1+jungleRatio)))*0.5);
        this.jungleLowerLeft = new Vector2d((int)(Math.floor(width/2))-x,(int)(Math.floor(height/2))-y);
        this.jungleUpperRight = new Vector2d((int)(width*0.5+x), (int)(height*0.5+y));
        this.animals=new ArrayList<>();
        for(int i=0; i<numberOfAnimals; i++)
        {
            ProjectAnimal animal=new ProjectAnimal(this, startEnergy);
            animals.add(animal);
        }
        this.plants=new ArrayList<>();
    }

    void removeDeadAnimals()
    {
        for(int i=0; i<animals.size(); i++)
        {
            ProjectAnimal animal=animals.get(i);
            if(animal.isDead())
            {
                animals.remove(animal);
            }
        }
    }

    Vector2d backToTheMap(Vector2d position)
    {
        int newX = 0;
        int newY = 0;
        if (position.x >= 0)
        {
            newX = position.x % (getUpperRight().x + 1);
        } else {
            newX = (getUpperRight().x + (1 + position.x) % (getUpperRight().x + 1));
        }
        if (position.y >= 0)
        {
            newY = position.y % (getUpperRight().y + 1);
        } else {
            newY = (getUpperRight().y + (1 + position.y) % (getUpperRight().y + 1));
        }
        return new Vector2d(newX, newY);
    }

    public Vector2d getLowerLeft()
    {
        return lowerLeft;
    }

    public Vector2d getUpperRight()
    {
        return upperRight;
    }

    public void simulate()
    {
        removeDeadAnimals();
        for(ProjectAnimal animal: animals)
        {
            animal.turn();
            animal.move();
            animal.lostEnergy(moveEnergy);
            //każde zwierzę się obraca, porusza i traci energię związaną z ruchem
        }
        //sortowanie zwierząt ze względu na energię//
        ArrayList<ProjectAnimal> sortedAnimals=new ArrayList<>();
        while(!animals.isEmpty())
        {
            ProjectAnimal animal = strongestAnimal();
            sortedAnimals.add(animal);
            animals.remove(animal);
        }
        animals.addAll(sortedAnimals);

        for(ProjectAnimal animal: animals)//jedzenie OD NAJSILNIEJSZEGO
        {
            Vector2d position = animal.getPosition();
            if (isOccupiedByPlant(position))
            {
                Plant plant=findPlantAtPosition(position);
                if(plant!=null)animal.eat(plant);
            }
        }
        //po jedzeniu znowu sortuję zwierzęta po energii
        sortedAnimals.clear();
        while(!animals.isEmpty())
        {
            ProjectAnimal animal = strongestAnimal();
            sortedAnimals.add(animal);
            animals.remove(animal);
        }
        animals.addAll(sortedAnimals);

        for(int i=0; i<animals.size(); i++)//rozmnażanie kolejność OD NAJSILNIEJSZEGO
        {
            ProjectAnimal animal=animals.get(i);
            Vector2d position=animal.getPosition();
            if(isOccupiedByAnotherAnimal(position, animal))
            {
                ProjectAnimal parentB=findAnotherParent(position, animal);//znajduję drugie najsilniejsze zwierzę na tym samym polu do rozmnażania
                if(parentB!=null)animal.makeABaby(parentB);
            }
        }

        Random rand=new Random();
        //(dopóki się nie uda) dodaj rośliny na losowych miejscach: jedna w dżungli i jedna na sawannie

        //losuj miejsce w dżungli//
        if(!everythingOccupiedByPlants(jungleLowerLeft, jungleUpperRight))
        {
            int xJungle = rand.nextInt(jungleUpperRight.x - jungleLowerLeft.x) + jungleLowerLeft.x;
            //losowa współrzędna 'x' w dżungli
            int yJungle = rand.nextInt(jungleUpperRight.y - jungleLowerLeft.y) + jungleLowerLeft.y;
            //losowa współrzędna 'y' w dżungli
            while (isOccupiedByPlant(new Vector2d(xJungle, yJungle)))
            //dopóki na wylosowanym miejscu jest inna roślina a jeszcze nie wszystkie miejsca są zarośnięte
            {
                xJungle = rand.nextInt(jungleUpperRight.x - jungleLowerLeft.x) + jungleLowerLeft.x;
                //losuję inny x
                yJungle = rand.nextInt(jungleUpperRight.y - jungleLowerLeft.y) + jungleLowerLeft.y;
                //losuję inny y
            }
            placePlant(new Vector2d(xJungle, yJungle));
        }
        //losuj miejsce na sawannie//
        if(!everythingOccupiedByPlants(lowerLeft, upperRight))
        {
            int xWorld = rand.nextInt(upperRight.x - lowerLeft.x) + lowerLeft.x;
            //losowa współrzędna 'x' na całej mapie
            int yWorld = rand.nextInt(upperRight.y - lowerLeft.y) + lowerLeft.y;
            //losowa współrzędna 'y' na całej mapie
            while (isOccupiedByPlant(new Vector2d(xWorld, yWorld)))
            //dopóki na wylosowanym miejscu jest inna roślina a jeszcze nie wszystkie miejsca są zarośnięte
            {
                xWorld = rand.nextInt(upperRight.x - lowerLeft.x) + lowerLeft.x;
                //losuję inny x
                yWorld = rand.nextInt(upperRight.y - lowerLeft.y) + lowerLeft.y;
                //losuję inny y
            }
            placePlant(new Vector2d(xWorld, yWorld));
        }
    }

    public void placePlant(Vector2d position)
    {
        Plant plant = new Plant(this, position);
        plants.add(plant);
    }

    public ProjectAnimal strongestAnimal()
    {
        ProjectAnimal strongest=animals.get(0);
        for(ProjectAnimal animal: animals)
        {
            if(animal.energy>strongest.energy) strongest=animal;
        }
        return strongest;
    }

    public Plant findPlantAtPosition(Vector2d position)
    {
        for(Plant plant: plants)
        {
            if(plant.position.equals(position)) return plant;
        }
        return null;
    }

    public ProjectAnimal findAnotherParent(Vector2d position, ProjectAnimal parentA)
    {
        ArrayList<ProjectAnimal> animalsAtPosition = new ArrayList<>();
        for(ProjectAnimal animal: animals)
        {
            if(animal.getPosition().equals(position) && animal!=parentA) animalsAtPosition.add(animal);
        }
        if(!animalsAtPosition.isEmpty())
        {
            ProjectAnimal strongest = animalsAtPosition.get(0);
            for(ProjectAnimal animal: animalsAtPosition)
            {if(animal.energy>strongest.energy) strongest=animal;}
            return strongest; //zwraca najsilniejsze zwierzę na tej samej pozycji: partnera do rozmnażania
        }
        return null;
    }

    public boolean isOccupiedByAnotherAnimal(Vector2d position, ProjectAnimal animal1)
    {
        position=backToTheMap(position);
        for(ProjectAnimal animal: animals)
        {
            if(animal.getPosition().equals(position) && animal!=animal1) return true;
        }
        return false;
    }

    public boolean isOccupiedByPlant(Vector2d position)
    {
        position=backToTheMap(position);
        for(Plant plant: plants)
        {
            if(plant.position.equals(position)) return true;
        }
        return false;
    }

    public boolean isOccupiedByAnimal(Vector2d position)
    {
        position=backToTheMap(position);
        for(ProjectAnimal animal: animals)
        {
            if(animal.getPosition().equals(position)) return true;
        }
        return false;
    }

    public boolean everythingOccupiedByPlants(Vector2d lowerL, Vector2d upperR)
    {
        for(int x=lowerL.x; x<=upperR.x; x++)
        {
            for (int y = lowerL.y; y <= upperR.y; y++)
            {
                if (!isOccupiedByPlant(new Vector2d(x, y))) return false;
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        return mapVisualizer.draw(getLowerLeft(), getUpperRight());
    }
}