import java.util.*;
import java.util.stream.Collectors;

class Dice  {
    int low;
    int high;
    int result;
    Dice(int low, int high){
        this.low = low;
        this.high = high;
    }
    int getResult(){
        return result;
    }

     int  play(){
        result = (new Random()).nextInt(high-low) + low;
        return result;
    }

}

class DiceSta{
    double occurs;
    double occured;
    DiceSta(){
        occurs = 0;
        occured = 0;
    }
}

class DictGameStatistic{
    final Map<Integer,DiceSta> staticMap = new HashMap<>() ;
    public void addStatistic(int score, int totolCount){
        DiceSta tmpdice =staticMap.get(score);
        if(tmpdice==null){
              tmpdice= new DiceSta();
        }
        tmpdice.occured++;
        tmpdice.occurs = tmpdice.occured/totolCount;
        staticMap.put(score,tmpdice);
    }

    public void output(int number,int dice){
        System.out.println("-----------");
        System.out.println(String.format("Number of simulations was  %d using %d dice ",number,dice));
        staticMap.forEach((k,diceSta)->{
            System.out.println(String.format("Total %d occurs %.2f occured %.2f",k,diceSta.occurs,diceSta.occured));
        });
    }
    public Map<Integer, DiceSta> getStaticMap() {
        return staticMap;
    }
}
class DiceGamePlayer{

    DictGameStatistic dictGameStatistic = new DictGameStatistic();
    public int findMinDice(List<Dice> diceList){
       int min = Integer.MAX_VALUE;
       Dice minDice = null;
       List<Dice> removeList= new ArrayList<>();
       for(Dice dice:diceList){
           if(min>dice.getResult()){
               min = dice.getResult();
               minDice = dice;
               removeList.add(minDice) ;
           }
           if(min==dice.getResult()){
               removeList.add(minDice) ;
           }
       }
       if(!removeList.isEmpty()){
           diceList.removeAll(removeList) ;
       }
       return min;
    }
    public void replayDiceList(List<Dice> diceList){
        diceList.forEach(dice -> {
            dice.play();
        });
    }

    public int collectScoreForOneRound(List<Dice> diceList){
        int score = 0;
        boolean flagFor3= false;
        List<Dice> removeList= new ArrayList<>();
        for(Dice dice:diceList){
            if(dice.result==3){
                flagFor3 = true;
                removeList.add(dice);
            }
        }

        if(!removeList.isEmpty()){
            diceList.removeAll(removeList);
        }

        if(flagFor3)
        {
            return 0;
        }else{
            if(!diceList.isEmpty()) {
                score +=findMinDice(diceList);
            }
        }
        return score;
    }
    public   List<Dice> startGame(int numDice,int minDice, int maxDice){
        List<Dice> diceList = new ArrayList<>();
        for (int i = 0; i <numDice ; i++) {
            Dice dice = new Dice(minDice,maxDice);
            dice.play();
            diceList.add(dice);
        }
        return diceList;
    }
}
public class DiceGame {

    public static int play(int numDice,int minDice, int maxDice){
        DiceGamePlayer diceGamePlayer = new DiceGamePlayer();
        List<Dice> ls =  diceGamePlayer.startGame(numDice,minDice,maxDice);
       // ls.forEach(dice->System.out.print(dice.getResult()));
        int totalScore =  0;
        while(ls.size()>0){
            int score = diceGamePlayer.collectScoreForOneRound(ls);
            totalScore += score;
            if(ls.size()==0) break;
            diceGamePlayer.replayDiceList(ls);
        }
        return totalScore;
    }
    public static void main(String[] args) {

        DictGameStatistic dictGameStatistic = new DictGameStatistic();
        int testCount =  100;
        int numberDice= 2;
        int minDice = 1;
        int maxDice = 10;
        for (int i = 0; i < testCount; i++) {
            int score = DiceGame.play(numberDice,minDice,maxDice);
            dictGameStatistic.addStatistic(score,testCount);
        }
        dictGameStatistic.output(testCount,numberDice);

    }
}
