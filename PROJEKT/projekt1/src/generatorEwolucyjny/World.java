package generatorEwolucyjny;

public class World
{
    public static void main(String args[])
    {
        int width=10;
        int height=10;
        double jungleRatio=0.5;
        int startEnergy=50;
        int moveEnergy=3;
        int plantEnergy=5;
        int numberOfAnimals=50;//ile zwierząt znajduje się na mapie na początku: zanim jeszcze zaczną umierać
        int daysOfSimulation=40; //później dość długo zajmuje losowanie niezajętego miejsca dla trawy
        ProjectMap map = new ProjectMap(width, height, jungleRatio, startEnergy, moveEnergy, plantEnergy, numberOfAnimals);
        System.out.println(map);
        while(daysOfSimulation>0)
        {
            System.out.println("End of the World in " + daysOfSimulation+ " day/s.");
            System.out.println("");
            map.simulate();
            System.out.println(map);
            daysOfSimulation--;
        }
        /**
        wyświetlane dane:
         A - zwierzę (jedno lub więcej na danej pozycji)
         . - roślina (jedna na danej pozycji)
         */
    }
}