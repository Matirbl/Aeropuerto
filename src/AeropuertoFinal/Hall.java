package AeropuertoFinal;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hall {


    private final Lock lock;
    private final Condition esperaPuestoA;
    private final Condition esperaPuestoB;
    private final Condition esperaPuestoC;

    public Hall() {
        this.lock = new ReentrantLock();
        esperaPuestoA = this.lock.newCondition();
        esperaPuestoB = this.lock.newCondition();
        esperaPuestoC = this.lock.newCondition();
    }


    public void esperarHall(int idAerolinea) {

        this.lock.lock();
        try {
            if (idAerolinea == 1) {
                esperaPuestoA.await();
            } else if (idAerolinea == 2) {
                esperaPuestoB.await();
            } else if (idAerolinea == 3) {
                esperaPuestoC.await();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Hall.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.lock.unlock();
        }
    }

    public void avisar(int idAerolinea) {

        this.lock.lock();
        try {
            if (idAerolinea == 1) {
                esperaPuestoA.signalAll();
            } else if (idAerolinea == 2) {
                esperaPuestoB.signalAll();
            } else if (idAerolinea == 3) {
                esperaPuestoC.signalAll();
            }
        } finally {
            this.lock.unlock();
        }
    }
}
