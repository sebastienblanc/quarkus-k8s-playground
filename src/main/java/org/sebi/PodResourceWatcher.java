package org.sebi;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;

public class PodResourceWatcher implements Watcher<Pod> {
    @Override
    public void eventReceived(Action action, Pod pod) {
        System.out.println("Received " + action.name() + " on pod " + pod.getMetadata().getName());
    }


    @Override
    public void onClose(WatcherException cause) {
        // TODO Auto-generated method stub
        
    }
}
