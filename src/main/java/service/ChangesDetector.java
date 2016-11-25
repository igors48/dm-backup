package service;

import static util.Assert.guard;
import static util.Parameter.isValidString;

/**
 * Created by igor on 25.11.2016.
 */
public class ChangesDetector {

    private final ContentRepository contentRepository;
    private final ChangesRepository changesRepository;

    public ChangesDetector(final ContentRepository contentRepository, final ChangesRepository changesRepository) {
        this.contentRepository = contentRepository;
        this.changesRepository = changesRepository;
    }

    public boolean isContentWasChanged(final String content) {
        guard(isValidString(content));

        final String latestSnapshot = this.contentRepository.loadLatestSnapshot();
        final boolean contentChanged = isContentChanged(latestSnapshot, content);

        if (contentChanged) {
            this.changesRepository.updateChangesTimestamp();

            return false;
        } else {
            return true;
        }
    }

    private boolean isContentChanged(final String oldContent, final String newContent) {
        return false;
    }

}
