package generatorEwolucyjny;

import agh.cs.lab5.*;
import java.util.Arrays;
import java.util.Random;

public class ProjectAnimal
{
    public int energy;
    private Vector2d position;
    private ProjectMap map;
    private LookDirection orientation;
    private int[] genes = new int[32];

    public ProjectAnimal(ProjectMap map, int startEnergy)
    {
        this.map=map;
        this.energy=startEnergy;
        Random rand=new Random();
        generateGenes();
        int orient = rand.nextInt(8);
        switch (orient) {//losowa orientacja nowego zwierzęcia
            case 0:
                this.orientation = LookDirection.N;
            case 1:
                this.orientation = LookDirection.NE;
            case 2:
                this.orientation = LookDirection.E;
            case 3:
                this.orientation = LookDirection.SE;
            case 4:
                this.orientation = LookDirection.S;
            case 5:
                this.orientation = LookDirection.SW;
            case 6:
                this.orientation = LookDirection.W;
            case 7:
                this.orientation = LookDirection.NW;
        }
        int xVec=rand.nextInt(this.map.getUpperRight().x-this.map.getLowerLeft().x)+this.map.getLowerLeft().x;
        int yVec=rand.nextInt(this.map.getUpperRight().y-this.map.getLowerLeft().y)+this.map.getLowerLeft().y;
        this.position=new Vector2d(xVec, yVec);//losowa pozycja nowego zwierzęcia;
    }

    public ProjectAnimal(ProjectMap map, Vector2d position, ProjectAnimal parentA, ProjectAnimal parentB)
    {
        Random rand = new Random();
        generateGenes(parentA.genes, parentB.genes);
        int orient = rand.nextInt(8);
        switch (orient) {//losowa orientacja nowego zwierzęcia
            case 0:
                this.orientation = LookDirection.N;
            case 1:
                this.orientation = LookDirection.NE;
            case 2:
                this.orientation = LookDirection.E;
            case 3:
                this.orientation = LookDirection.SE;
            case 4:
                this.orientation = LookDirection.S;
            case 5:
                this.orientation = LookDirection.SW;
            case 6:
                this.orientation = LookDirection.W;
            case 7:
                this.orientation = LookDirection.NW;
        }
        this.position = position;
        this.map = map;
    }

    private void generateGenes()
    {
        Random rand=new Random();
        int a; int i;
        for(i=0; i<8; i++)
        {genes[i]=i;} //żeby mieć pewność, że żadne geny nie wyginą
        while(i<32) //uzupełnienie pozostałych genów zwierzęcia
        {
            a = rand.nextInt(8);//losowanie liczby z przedziału 0-7
            genes[i] = a;
            i++;
        }
        Arrays.sort(genes);
    }

    private void generateGenes(int[] genesA, int[] genesB)
    {
        Random rand=new Random();
        int div1=rand.nextInt(30)+1; //losowanie pierwszego indeksu podziału
        //liczba z zakresu od 1 do 30 włącznie
        int div2=rand.nextInt(31-div1)+div1; //drugi indeks podziału
        System.arraycopy(genesA, 0, genes, 0, div1+1);
        System.arraycopy(genesB, div1+1, genes, div1+1, div2-div1);
        System.arraycopy(genesA, div2+1, genes, div2+1, genes.length-div2-1);
        Arrays.sort(genes);
        while(extinct(genes))
        {
            for(int i=0; i<8; i++)//dla każdego genu sprawdzamy, czy nie wyginął, jeżeli wyginął, to dodajemy w losowym miejscu
            {
                int index = 0;
                while (genes[index] < i) {
                    index++;
                }
                if (genes[index] != i) {
                    index = rand.nextInt(32);
                    genes[index] = i;//zabezbieczenie przed wyginięciem
                }
            }
            Arrays.sort(genes);
        }
    }

    public boolean canReproduce()
    {
        return 2*energy>=this.map.startEnergy;
    }

    public boolean extinct (int[] genes)
    {
        int index=0;
        int j=0;
        while(j<8)
        {
            while (index < 32 && genes[index]<j)
            {
                if(genes[index]<j)
                index++;
            }
            if(genes[index]==j){j++;}
            else return true; //czyli jakiś gen wyginął
        }
        return false;
    }

    public void makeABaby(ProjectAnimal parentB) //jako parentA
    {
        if(this.canReproduce() && parentB.canReproduce())
        {
            Random rand=new Random();//losowe pole dla dziecka
            int bx=rand.nextInt(3)-1;//-1 lub zero lub 1
            int by=rand.nextInt(3)-1;
            Vector2d babyPosition=new Vector2d(bx, by);
            if(map.isOccupiedByAnotherAnimal(babyPosition.add(this.getPosition()), this))
            {
                energy=(int) Math.floor(0.75 * energy);
                parentB.energy=(int) Math.floor(0.75 * parentB.energy);
                ProjectAnimal baby=new ProjectAnimal(map, babyPosition, this, parentB);
                map.animals.add(baby);//dodaję dziecko do listy zwierząt na mapie
            }
        }
    }

    public void lostEnergy(int moveEnergy)
    {
        this.energy -= moveEnergy;
    }

    public void increaseEnergy(int plantEnergy)
    {
        this.energy += plantEnergy;
    }

    public void eat(Plant plant)
    {
        increaseEnergy(this.map.energyFromPlant);
        map.plants.remove(plant);
    }

    public Vector2d getPosition()
    {
        return position;
    }

    public boolean isDead()
    {
        return energy <= 0;
    }

    public void turn()
    {
        Random rand=new Random();//losowanie w którą stronę zwierzę się obróci
        int index=rand.nextInt(32); //liczba z zzakresu 0-31: losowy indeks z tablicy genów zwierzęcia
        //szansa na wybranie danej strony jest proporcjonalna do liczby genów reprezentujących dany obrót
        int turns=genes[index];
        while(turns>0)
        {
            this.orientation=this.orientation.next();//zwierzę obraca się tyle razy, ile każe mu wylosowany gen
            turns--;
        }
    }

    public void move()
    {
        this.position = this.getPosition().add(this.orientation.toUnitVector());
        this.position = this.map.backToTheMap(this.position);
        //jeżeli zwierzę podczas ruchu wyszło poza mapę
    }
}