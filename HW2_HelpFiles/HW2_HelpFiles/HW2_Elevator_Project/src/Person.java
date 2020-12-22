
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
public class Person extends Thread {

    // CONSTANTS
    private final int ON_FLOOR = 1;
    private final int WAITING = 2;
    private final int ON_ELEVATOR = 3;
    
    private GUI myApp;
    private Elevator myElevator;
    private int personID = 0;
    private int status = ON_FLOOR;
    private int currentFloor = 1;
    private int destFloor = -1;
    
    public Person(int i, GUI a, Elevator e){
        personID = i;
        myApp = a;
        myElevator = e;
        
    }
    
    public void callElevator()
    { 
        if(status == ON_FLOOR)
        {
            status = WAITING;
            myApp.updatePersonStatus(personID, "Waiting");
            myElevator.calledToFloor(currentFloor);
        }
        
    }
    
    public void setDestinationFloor(int floor)
    {
        if(status == ON_ELEVATOR)
        {
            destFloor = floor;
            myApp.updatePersonStatus(personID, "Destination Floor set to " + destFloor);
            myElevator.calledToFloor(destFloor);
           
        }
    }
    
    public void run() {
        while (myApp.continueRunning()) // check with GUI App to see if running should continue
        {
            if (status == ON_FLOOR)
            {
                
            }
            else if (status == WAITING)
            {
                if (currentFloor == myElevator.getCurrentFloor())
                {
                    status = ON_ELEVATOR;
                    myApp.updatePersonStatus(personID, "On Elevator");
                }
                
            }
            else // ON_ELEVATOR 
            {
                if (destFloor == myElevator.getCurrentFloor())
                {
                    status = ON_FLOOR;
                    currentFloor = destFloor;
                    myApp.updatePersonStatus(personID, "On Floor " + currentFloor);
                }
            }
            
             try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
