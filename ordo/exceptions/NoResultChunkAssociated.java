package ordo.exceptions;

import hdfs.ChunkIdentifier;

public class NoResultChunkAssociated extends HidoopFail{
    public NoResultChunkAssociated(ChunkIdentifier ci){
        super("Le fichier hdfs " + ci.getResultLocalFname() + " n'a pas de fichier resultat associé.");
    }
}
