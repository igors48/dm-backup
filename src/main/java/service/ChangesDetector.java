package service;

import static util.Assert.guard;
import static util.Parameter.isValidString;

/**
 * Created by igor on 25.11.2016.
 */
public class ChangesDetector {

    private final ContentRepository contentRepository;
    private final ChangesRepository changesRepository;
    private final TimeService timeService;

    public ChangesDetector(final ContentRepository contentRepository, final ChangesRepository changesRepository, final TimeService timeService) {
        this.contentRepository = contentRepository;
        this.changesRepository = changesRepository;
        this.timeService = timeService;
    }

    public boolean isContentWasChanged(final String content) {
        guard(isValidString(content));

        final String latestSnapshot = this.contentRepository.loadLatestSnapshot();
        final boolean contentChanged = isContentChanged(latestSnapshot, content);
        final ChangesTimestamp changesTimestamp = this.changesRepository.load();

        if (contentChanged) {
            //TODO not from here
            updateTimestamp(changesTimestamp);

            return false;
        } else {
            updateContent(content, changesTimestamp);

            return true;
        }
    }

    private void updateContent(final String content, final ChangesTimestamp changesTimestamp) {

    }

    private void updateTimestamp(final ChangesTimestamp changesTimestamp) {
        final long current = this.timeService.currentTimestamp();
        final ChangesTimestamp updatedTimestamp = changesTimestamp.setTimestamp(current);
        this.changesRepository.store(updatedTimestamp);
    }

    private boolean isContentChanged(final String oldContent, final String newContent) {
        return false;
    }

}
