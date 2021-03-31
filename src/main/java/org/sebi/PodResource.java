package org.sebi;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.TaintBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

@Path("/pod")
public class PodResource {

    private final KubernetesClient kubernetesClient;

    public PodResource(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @GET
    @Path("/{namespace}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pod> pods(@PathParam("namespace") String namespace) {
        //kubernetesClient.pods().watch(new PodResourceWatcher());
        kubernetesClient.nodes().withLabel("node-role.kubernetes.io/master").list().getItems().forEach(
            (node) -> {
                addTaint(node);
                System.out.println(node.getMetadata().getName() + ":" + node.getMetadata().getLabels().get("beta.kubernetes.io/arch"));
            }
        );;
        return kubernetesClient.pods().inNamespace(namespace).list().getItems();
    }

    private void addTaint(Node node){
        String nodeName = node.getMetadata().getName();
        TaintBuilder builder = new TaintBuilder();
                builder.withKey("mytaint").withEffect("NoSchedule");
        kubernetesClient.nodes().withName(nodeName).edit(n -> new NodeBuilder(node)
        .editSpec()
            .addNewTaintLike(builder.build())
            .endTaint()
        .endSpec()
        .build());
        }
}