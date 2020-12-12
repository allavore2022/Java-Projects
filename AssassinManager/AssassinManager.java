import java.util.*;
/*
   Alisa Llavore
   AssassinManager.java
   November 23, 2019
   This program will mimick the game Assassin.
*/
public class AssassinManager {
   // YOUR CODE GOES HERE
   
   //instance varible
   private AssassinNode frontKill;
   private AssassinNode frontGraveyard;
   
   public AssassinManager(List<String> names){
      if(names == null || names.size() == 0){
         throw new IllegalArgumentException();
      }
      
      for(int i = 0; i < names.size(); i++){
         //get name of player
         String name = names.get(i);
         //create new node for player
         AssassinNode player = new AssassinNode(name);
         
         //check to see if front is null. If yes set frontKill to player
         if(frontKill == null){
            frontKill = player;
         }else {
            AssassinNode current = frontKill;
            while(current.next != null){
               current = current.next;
            }
            current.next = player;
         }
      }
   }
    
   public void printKillRing(){
      //System.out.println("Current kill ring: ");
      AssassinNode current = frontKill;
      while(current != null){
         //check to see if next node is null (aka if this is the last node)
         //have it stalk the first node
         if(current.next == null){
            System.out.println("\t" + current.name + " is stalking " + frontKill.name);
         }else {
            System.out.println("\t" + current.name + " is stalking " + current.next.name);
         }
         current = current.next;
       }
   }
    
   public void printGraveyard(){
      AssassinNode current = frontGraveyard;
      while(current != null){
         //check to see if next node is null (aka if this is the last node)
         //have it stalk the first node
         if(current.next == null){
            System.out.println(current.name + " was killed by " + current.killer);
         }else {
            System.out.println(current.name + " is stalking " + current.killer);
         }
         current = current.next;
      }
   }
    
   public boolean killRingContains(String name){
      AssassinNode current = frontKill;
      while(current != null){
         if(current.name.equalsIgnoreCase(name)){
            return true;
         }
         current = current.next;
      }
      return false;
   }
    
   public boolean graveyardContains(String name){
      AssassinNode current = frontGraveyard;
      while(current != null){
         if(current.name.equalsIgnoreCase(name)){
            return true;
         }
         current = current.next;
      }
      return false;
   }
    
   public boolean isGameOver(){
      if(frontKill.next == null)
         return true;
      else
         return false;
   }
    
   public String winner(){
      if(isGameOver() == false){
         return null;
      }else{
         return frontKill.name;
      }
   }
    
   public void kill(String name){
      if(isGameOver() == true){
         throw new IllegalStateException();
      }
      if(!killRingContains(name)){
         throw new IllegalArgumentException();
      }else{
         AssassinNode prev = frontKill;
         AssassinNode current = frontKill.next;
         while(current != null){
            if(current.next.name.equals(name)){
            }
         }
      }
      
   }
    

    /***************** DO NOT MODIFY AssassinNode   **************************/
    /**
     * Each AssassinNode object represents a single node in a linked list
     * for a game of Assassin.
     */
    private static class AssassinNode 
    {
        public final String name;  // this person's name
        public String killer;      // name of who killed this person (null if alive)
        public AssassinNode next;  // next node in the list (null if none)
        
        /**
         * Constructs a new node to store the given name and no next node.
         */
        public AssassinNode(String name) 
        {
            this(name, null);
        }

        /**
         * Constructs a new node to store the given name and a reference
         * to the given next node.
         */
        public AssassinNode(String name, AssassinNode next)
        {
            this.name = name;
            this.killer = null;
            this.next = next;
        }
    }
}