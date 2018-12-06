package ordo.exceptions;

import hdfs.ChunkIdentifier;

public class ResultChunkAssociatedError extends HidoopUnpFail {
    public ResultChunkAssociatedError(ChunkIdentifier ci){
        super("Le fichier hdfs " + ci.getResultLocalFname() + " n'a pas de fichier resultat associé.");
    }
}
