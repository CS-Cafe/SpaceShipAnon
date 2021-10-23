package Engine.Animation;

import Engine.Utility.Image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO:
 *  FIX YOUR BUILDER MUTABILITY LEAK YOU CAN CLEARLY MUTATE
 *  THE ARRAY-LIST AFTER INSTANTIATION FROM THE BUILDER !!!!
 * Reel
 *
 * A {@code Reel} is an immutable (nope!) data-structure. A {@code Reel}
 * stores animation frames contiguously and allows the client to paint
 * a single frame per instance. A {@code Reel} gives access to the next
 * frame only through the instantiation of a new {@code Reel}.
 *
 * @author Ellie Moore
 * @version 11.23.2020
 */
public class Reel {

    /**
     * A null-safe {@code Reel}.
     */
    public static final Reel NULL = new Reel() {
        @Override
        public boolean isNull(){
            return true;
        }
    };

    /**
     * The frames belonging to this {@code Reel}.
     */
    private final List<BufferedImage> frames;

    /**
     * The current frame of this {@code Reel}.
     */
    private final int currentFrame;

    /**
     * Whether or not this {@code Reel} should loop.
     */
    private final boolean isLooping;

    /**
     * A private constructor for a {@code Reel}.
     *
     * @param builder the builder to use
     */
    private Reel(final Builder builder){
        this.frames = Collections.unmodifiableList(builder.frames);
        this.currentFrame = -1;
        this.isLooping = builder.isLooping;
    }

    /**
     * A secondary, private constructor for updating a {@code Reel}.
     *
     * @param r the {@code Reel} to be updated
     */
    private Reel(final Reel r){
        this.frames = r.frames;
        this.isLooping = r.isLooping;
        this.currentFrame = (r.currentFrame < r.frames.size() - 1)?
                r.currentFrame + 1: (r.isLooping? 0: r.currentFrame);
    }

    /**
     * A tertiary, private constructor to instantiate an empty
     * {@code Reel}.
     */
    private Reel(){
        this.frames = Collections.emptyList();
        this.currentFrame = -1;
        this.isLooping = false;
    }

    /**
     * A method to return a new {@code Reel} with the next frame
     * as the current frame.
     *
     * @return a new {@code Reel} with the next frame selected
     */
    public final Reel nextFrame(){
        if(isNull()) return this;
        return new Reel(this);
    }

    /**
     * A method to paint the current frame.
     *
     * @param g2 the {@code Graphics2D} object to use
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the desired width
     * @param height the desired height
     */
    public final void paintFrame(final Graphics2D g2, final int x, final int y,
                           final int width, final int height){
        if(!frames.isEmpty() && currentFrame >= 0)
            g2.drawImage(frames.get(currentFrame), x, y, width, height, null);
    }

    /**
     * A method to indicate whether or not this {@code Reel} is null.
     *
     * @return whether or not this {@code Reel} is null
     */
    public boolean isNull(){
        return false;
    }

    /**
     * Builder
     *
     * A Builder Patten for use in constructing a {@code Reel}.
     */
    public static final class Builder {

        /**
         * The frames belonging to this {@code Builder}.
         */
        private final List<BufferedImage> frames;

        /**
         * Whether or not the Reel should reset to frame 0 upon
         * reaching the final frame.
         */
        private boolean isLooping;

        /**
         * A public constructor for a {@code Builder}.
         */
        public Builder(){
            frames = new ArrayList<>();
            isLooping = false;
        }

        /**
         * A method to add a frame to this {@code Builder} via
         * path name.
         *
         * @param imagePath the path name
         * @return the instance
         */
        public Builder addFrame(final String imagePath){
            frames.add(Image.read(imagePath));
            return this;
        }

        /**
         * A method to add a frame to this {@code Builder}.
         *
         * @param image the {@code BufferedImage} to use as a frame.
         * @return the instance
         */
        public Builder addFrame(final BufferedImage image){
            frames.add(image);
            return this;
        }

        /**
         * A method to specify whether or not the built {@code Reel}
         * should reset to frame 0 after reaching its final frame.
         *
         * @param isLooping
         * @return
         */
        public Builder setIsLooping(final boolean isLooping){
            this.isLooping = isLooping;
            return this;
        }

        /**
         * A method to instantiate a {@code Reel} from the properties
         * stored within this {@code Builder}.
         * @return
         */
        public Reel build(){
            return new Reel(this);
        }

    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString(){
        return frames.toString();
    }

}
