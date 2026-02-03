package br.gov.mt.seplag.listener;

import br.gov.mt.seplag.dto.album.AlbumEventResponse;
import br.gov.mt.seplag.entity.Album;
import br.gov.mt.seplag.event.AlbumCriadoEvent;
import br.gov.mt.seplag.service.album.AlbumService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AlbumCriadoListener {

    private final AlbumService albumService;
    private final SimpMessagingTemplate messagingTemplate;

    public AlbumCriadoListener(final AlbumService albumService,
                               final SimpMessagingTemplate messagingTemplate) {
        this.albumService = albumService;
        this.messagingTemplate = messagingTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAlbumCriado(final AlbumCriadoEvent event) {
        final Album album = albumService.buscarPorId(event.getIdAlbum());

        messagingTemplate.convertAndSend(
            "/topic/novo-album",
            new AlbumEventResponse(album.getId(), album.getNome())
        );
    }

}