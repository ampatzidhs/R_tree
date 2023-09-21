public class Search {
    MBR anazitoumeno;


    public Search(MBR anazitoumeno) {
        this.anazitoumeno = anazitoumeno;
    }

    public void epikalici(CreateRTree tree)
    {
        for(Nodes nodes: tree.allNodes)
        {
            nodes.printNodes();
        }
    }





}
