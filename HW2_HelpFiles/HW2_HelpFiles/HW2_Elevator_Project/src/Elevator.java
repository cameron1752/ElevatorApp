
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dls102
 */
public class Elevator extends Thread {

    // CONSTANTS
    private final int IDLE = 1;
    private final int UP = 2;
    private final int DOWN = 3;
    
    private GUI myApp;
    private int status = IDLE;
    private int currentFloor = 1; // set to one to get started
    private int maxFloor = 11;
    private int minFloor = 1;
    
    public Elevator(GUI a)
    {
        myApp = a;
        
    }
    
    public synchronized int getCurrentFloor()
    {
        return currentFloor;
    }
    
    public synchronized void calledToFloor(int floor)
    {
        // need code to either set min/max floor or set field in array
        // change out of idle status, set new max and min floor, change direction if needed
    }
    
    public void run() {
        
        while (myApp.continueRunning()) // check with GUI App to see if running should continue
        {
            
            
            if (status == IDLE)
            {
                 myApp.updateElevatorStatus(currentFloor, "IDLE" );
            }
            else if (status == UP)
            {
                if (currentFloor < maxFloor){
                   currentFloor++;
                   myApp.updateElevatorStatus(currentFloor, "UP" );
                }
                else 
                {
                    // modify logic for setting to idle in future
                    
                    status = DOWN;
                    myApp.updateElevatorStatus(currentFloor, "DOWN" );
                }
                 
            }
            else // DOWN
            {
                if (currentFloor > minFloor){
                   currentFloor--;
                   myApp.updateElevatorStatus(currentFloor, "DOWN" );
                }
                else
                {
                     // modify logic for setting to idle in future
                    
                    status = UP;
                    myApp.updateElevatorStatus(currentFloor, "UP" );
                }
                
            }
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }
    }
}
