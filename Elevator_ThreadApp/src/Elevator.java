 
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cammy
 */
public class Elevator extends Thread {

    // CONSTANTS
    private final int IDLE = 1;
    private final int UP = 2;
    private final int DOWN = 3;
    
    private ElevatorFE myApp;
    private int status = IDLE;
    private int currentFloor = 5; // set to one to get started
    private int maxFloor = 11;
    private int minFloor = 1;
    private int idleFloor = 5;
    private int callQueue[][] = new int[6][3];
    
    public Elevator(ElevatorFE a)
    {
        myApp = a;
        callQueue[0][1] = 1;
        
    }
    
    /*
    
    * The stop function sets the status to idle and resets the callQueue variables for each person
         
     */
    public synchronized void stopElevator(){
        status = IDLE;
        for (int i = 0; i < 6; i++){
            callQueue[i][1] = 1;
            callQueue[i][2] = 0;
        }
    }
    
    /*
    
    * Function returns current floor of elevator
    
    */
    public synchronized int getCurrentFloor()
    {
        return currentFloor;
    }
    
    /*
    
    * Function returns true if elevator has been called and also prints call validation and destination floor
    
    */
    public synchronized boolean getQueue(){
        int trueInt = 0;
        for (int i = 0; i < 6; i++){
            if (callQueue[i][1] == 0){
                trueInt++;
            }
            System.out.println(callQueue[i][1] + " " + callQueue[i][2]);
        }
        if (trueInt > 0){
            return true;
        } else {
            return false;
        }
    }
    
    /*
    
    * Function handles the status change and min floor max floor changes when called 
    * by a Person.  
    
    */
    public synchronized void calledToFloor(int personID, int floor)
    {
        // need code to either set min/max floor or set field in array
        // change out of idle status, set new max and min floor, change direction if needed
        if (status == IDLE){
            // set was called for the call queue for each person that called
            setWasCalled(personID, true, floor);
            // if we're at the current floor that's been requested
            if (getCurrentFloor() == floor){
                status = IDLE;
                // if we're below the requested floor go to that floor
            } else if (getCurrentFloor() < floor){
                status = UP;
                maxFloor = floor;
                myApp.updateElevatorStatus(currentFloor, "UP");
                // if we're above the requested floor go to that floor
            } else {
                status = DOWN;
                minFloor = floor;
                myApp.updateElevatorStatus(currentFloor, "DOWN");
            }
            // if we're already headed up and we're called
        } else if (status == UP){
            // if we're below the destination floor
            if (floor > currentFloor){
                // call the floor for the person
                setWasCalled(personID, true, floor);
                // if we're here set to idle
                if (currentFloor == floor){
                    status = IDLE;
                    // if we're below the destination floor send it up
                } else if (currentFloor < floor){
                    // current floor of the elevator is lower than destination floor
                    status = UP;
                    maxFloor = floor;
                    myApp.updateElevatorStatus(currentFloor, "UP");
                    // else we're below it, send her down!
                } else {
                    status = DOWN;
                    minFloor = floor;
                    myApp.updateElevatorStatus(currentFloor, "DOWN");
                }
            } else {
                // call the floor
                setWasCalled(personID, true, floor);
                // see if there are additional higher floors to go to
                if (currentFloor == maxFloor){
                    // wait until we finish our current
                    status = DOWN;
                    minFloor = floor;
                    myApp.updateElevatorStatus(currentFloor,"DOWN");
                } else {
                    status = UP;
                    myApp.updateElevatorStatus(currentFloor, "UP");
                }
            }
            
            
        } else{
             if (floor > currentFloor){
                 // call the elevator to that floor for that person
                setWasCalled(personID, true, floor);
                // if we're already here set to idle
                if (currentFloor == floor){
                    status = IDLE;
                // current floor of the elevator is lower than destination floor
                } else if (currentFloor < floor){
                    status = UP;
                    maxFloor = floor;
                    myApp.updateElevatorStatus(currentFloor, "UP");
                    // if we're higher than the destination floor sender down
                } else {
                    status = DOWN;
                    minFloor = floor;
                    myApp.updateElevatorStatus(currentFloor, "DOWN");
                }
            } else {
                // call it to that floor for that person
                setWasCalled(personID, true, floor);
                // if we're above the called floor go down
                if (currentFloor > floor){
                    status = DOWN;
                    minFloor = floor;
                    myApp.updateElevatorStatus(currentFloor,"DOWN");
                    // if we're below it send the elevator up up  up
                } else {
                    status = UP;
                    maxFloor = floor;
                    myApp.updateElevatorStatus(currentFloor, "UP");
                }
            }
        }
    }
    
/*

* Function accesses the call queue with input person ID    
    
*/
public synchronized int getFloorCalled(int personID){
    return callQueue[personID][2];
}
    
/*

* Function sets call Queue given input person ID, true / false value for callBack and destination floor

*/
public void setWasCalled(int personID, boolean callBack, int destFloor){
    if (callBack){
        callQueue[personID][1] = 0;
        callQueue[personID][2] = destFloor;
    } else {
        callQueue[personID][1] = 1;
        callQueue[personID][2] = destFloor;
    }
}
   
    
    public void run() {
        
        while (myApp.continueRunning()) // check with GUI App to see if running should continue
        {
            // if the status is idle
            if (status == IDLE){
                myApp.updateElevatorStatus(currentFloor, "IDLE" );
                // reset max/min
                maxFloor = 11;
                minFloor = 1;
                System.out.println(getQueue());
                // if no one is in the call queue go to idle floor
                if (getQueue() == false){
                    // if we're below idle floor go up
                    if (currentFloor < idleFloor){
                        status = UP;
                        myApp.updateElevatorStatus(currentFloor, "UP" );
                        maxFloor = idleFloor;
                        // if we're above idle floor go down
                    } else if (currentFloor > idleFloor){
                        status = DOWN;
                        myApp.updateElevatorStatus(currentFloor, "DOWN" );
                        minFloor = idleFloor;
                        // if we're at the idle floor stay here
                    } else {
                        status = IDLE;
                    }
                    // if there is someone in the queue
                } else{
                    // set max difference in floors
                    int lowest = 11;
                    // set i value for next call person ID
                    int difference = 0;
                    // for each of the values in callQueue
                    for (int i = 0; i < 6; i++){
                        // if it's 0 they didn't call the elevator, do nothing
                        if (callQueue[i][2] == 0){
                            // if the called floor is actually in our building
                        } else if(callQueue[i][2] > 0 && callQueue[i][2] < 12){
                            // if called floor is greater than current floor
                            if ((callQueue[i][2] > currentFloor)){
                                // if the difference in called floor and where we currently are is lower than our lowest differnece in floors
                                if ((callQueue[i][2] - currentFloor) < lowest){
                                    lowest = callQueue[i][2] - currentFloor;
                                    difference = i;
                                }
                                // if called floor is less than current floor
                            } else if (callQueue[i][2] < currentFloor){
                                // if the difference is lower than our lowest low set it as our new low
                                if ((currentFloor - callQueue[i][2]) < lowest){
                                    lowest = currentFloor - callQueue[i][2];
                                    difference = i;
                                }
                            }
                        }    
                    }
                    // if the next floor isn't 0
                    if (callQueue[difference][2] != 0){
                        // if we're below it, send the elevator up
                        if (callQueue[difference][2] > currentFloor){
                            status = UP;
                            maxFloor = callQueue[difference][2];
                            myApp.updateElevatorStatus(currentFloor, "UP");
                            // if we're above it, send the elevator down
                        } else if(callQueue[difference][2] < currentFloor){
                            status = DOWN;
                            minFloor = callQueue[difference][2];
                            myApp.updateElevatorStatus(currentFloor, "DOWN");
                            // if we're here already set it to idle
                        }else{
                           status = IDLE; 
                        }
                    }
                }
            }
            else if (status == UP){
                // if we're still below the destination floor keep movin up
                if (currentFloor < maxFloor){
                   currentFloor++;
                   myApp.updateElevatorStatus(currentFloor, "UP" );
                   // if we're at it start to IDLE for next floor
                } else {
                    if (currentFloor == maxFloor){
                        status = IDLE;
                        myApp.updateElevatorStatus(currentFloor, "IDLE");
                    }
                }     
            }
            else {
                // if we're still above the destination floor keep goin down
                if (currentFloor > minFloor){
                   currentFloor--;
                   myApp.updateElevatorStatus(currentFloor, "DOWN" );
                   // else set to idle for next floor processing
                } else{
                    if (currentFloor == minFloor){
                        status = IDLE;
                        myApp.updateElevatorStatus(currentFloor, "IDLE");
                    }    
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