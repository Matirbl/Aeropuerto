package AeropuertoFinal;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hall {

    private Lock lock;
    private Condition [] hall;

    public Hall(int cantAerolineas) {
        this.lock = new ReentrantLock();
        hall= new Condition[cantAerolineas];

        for (int i = 0; i < cantAerolineas; i++) {
            this.hall[i] = lock.newCondition();
        }

    }

    public void esperarHall(int idAerolinea) {

        this.lock.lock();
        try {
           this.hall[idAerolinea].await();

        } catch (InterruptedException ex) {
            Logger.getLogger(Hall.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.lock.unlock();
        }
    }

    public void avisar(int idAerolinea) {
        System.out.println("El guardia avisa que hay lugar para el puesto " + idAerolinea);
        this.lock.lock();
        try {
            this.hall[idAerolinea].signalAll();
        } finally {
            this.lock.unlock();
        }
    }
}
