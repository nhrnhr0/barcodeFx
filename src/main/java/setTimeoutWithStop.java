public class setTimeoutWithStop implements Runnable {
    private boolean exit;
    Thread t;
    Runnable code;
    int delay;

    setTimeoutWithStop(int delay,  Runnable code) {
        t = new Thread(this);
        this.code = code;
        this.delay = delay;
        System.out.println("New thread: " + t);
        exit = false;
        t.start(); // Starting the thread

    }
    public void run()
    {
        try {
            Thread.sleep(delay);
            if(exit == false) {
                System.out.println("running code");
                code.run();
            }else {
                System.out.println("running code is canceled");
            }
        } catch (InterruptedException e) {
            System.out.println("Caught:" + e);
        }
        System.out.println("Thread ended.");

    }

    // for stopping the thread
    public void stop()
    {
        exit = true;
    }
}
