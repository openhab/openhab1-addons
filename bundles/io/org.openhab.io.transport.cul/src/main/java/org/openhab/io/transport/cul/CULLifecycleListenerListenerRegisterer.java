package org.openhab.io.transport.cul;

public class CULLifecycleListenerListenerRegisterer implements CULLifecycleListener {

    private CULListener listener;

    public CULLifecycleListenerListenerRegisterer(CULListener listener) {
        this.listener = listener;
    }

    @Override
    public void open(CULHandler cul) throws CULCommunicationException {
        cul.registerListener(listener);

    }

    @Override
    public void close(CULHandler cul) {
        cul.unregisterListener(listener);
    }

}
