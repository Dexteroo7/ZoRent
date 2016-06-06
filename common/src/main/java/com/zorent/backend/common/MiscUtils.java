package com.zorent.backend.common;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.GeoPt;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.googlecode.objectify.Key;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by dexter on 24/01/16.
 */
public enum MiscUtils {

    ;

    public static final Blob EMPTY_BLOB = new Blob(new byte[]{});

    public static long longHash(String string) {

        final int len = string.length();
        long h = 1125899906842597L; // prime

        for (int i = 0; i < len; i++)
            h = 31 * h + string.charAt(i);

        return h;
    }

    public static void closeQuietly(Closeable... closeables) {
        for (Closeable closeable : closeables)
            if (closeable != null)
                try {
                    closeable.close();
                } catch (IOException ignored) {
                }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null)
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
    }

    public static <T, E> Iterable<Key<T>> getKeyBuilder(final Class<T> type, Iterable<E> ids) {

        return FluentIterable.from(ids)
                .transform(new Function<E, Key<T>>() {
                    @Nonnull
                    @Override
                    public Key<T> apply(@Nullable E id) {

                        if (id instanceof Long) {

                            final long long_id = (Long) id;
                            if (long_id > 0)
                                return Key.create(type, (Long) id);
                            else
                                throw new IllegalArgumentException("Invalid long id, null or 0");

                        } else if (id instanceof String) {

                            final String stringId = (String) id;
                            if (TextUtils.isNotEmpty(stringId))
                                return Key.create(type, (String) id);
                            else
                                throw new IllegalArgumentException("Invalid string id, null or empty");

                        } else
                            throw new IllegalArgumentException("Only long and string ids are supported");
                    }
                });
    }

    public static <T> Iterable<Key<T>> getKeyBuilder(Class<T> type, Long... ids) {

        return getKeyBuilder(type, Iterables.cycle(ids));
    }

    public static <T> Iterable<Key<T>> getKeyBuilder(Class<T> type, String... ids) {

        return getKeyBuilder(type, Iterables.cycle(ids));
    }

    public static <T> String seqToString(Iterable<T> items) {

        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean needSeparator = false;
        for (T x : items) {
            if (needSeparator)
                sb.append(' ');
            sb.append(x.toString());
            needSeparator = true;
        }
        sb.append(']');
        return sb.toString();
    }

    public static <T extends Enum> String enumToString(Iterable<T> items) {

        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean needSeparator = false;
        for (T x : items) {
            if (needSeparator)
                sb.append(',');
            sb.append(x.name());
            needSeparator = true;
        }
        sb.append(']');
        return sb.toString();
    }

    public static ThreadPoolExecutor getRejectionExecutor() {

        //an executor for getting stories from server
        return new ThreadPoolExecutor(
                1, //only 1 thread
                1, //only 1 thread
                0L, TimeUnit.MILLISECONDS, //no waiting
                new SynchronousQueue<Runnable>(false), //only 1 thread
                new ThreadPoolExecutor.DiscardPolicy()); //ignored
    }

    public static ThreadPoolExecutor getRejectionExecutor(ThreadFactory threadFactory) {

        //an executor for getting stories from server
        return new ThreadPoolExecutor(
                1, //only 1 thread
                1, //only 1 thread
                0L, TimeUnit.MILLISECONDS, //no waiting
                new SynchronousQueue<Runnable>(false), //only 1 thread
                threadFactory,
                new ThreadPoolExecutor.DiscardPolicy()); //ignored
    }

    public static final class CopyAndForward extends FilterInputStream {

        public static CopyAndForward newInstance(InputStream mainStream,
                                                 OutputStream fileOut) {

            return new CopyAndForward(mainStream, fileOut);
        }

        //when a read is performed we also write it out to the fileOut
        private final OutputStream forwardTo;

        private CopyAndForward(InputStream mainStream, OutputStream forwardTo) {
            super(mainStream);
            this.forwardTo = forwardTo;
        }

        @Override
        public int read() throws IOException {

            final byte[] singleByte = new byte[1];
            final int read = super.read(singleByte);
            if (read != -1) {

                forwardTo.write(singleByte); //forward the bytes
                return -1;
            } else
                return singleByte[0];
        }

        @Override
        public int read(@Nonnull byte[] b) throws IOException {

            final int read = super.read(b);
            if (read != -1)
                forwardTo.write(b); //forward the bytes
            return read;
        }

        @Override
        public int read(@Nonnull byte[] b, int off, int len) throws IOException {

            final int read = super.read(b, off, len);
            if (read != -1)
                forwardTo.write(b, off, len); //forward the bytes
            return read;
        }

        @Override
        public long skip(long n) {
            throw new UnsupportedOperationException("Can't skip, sorry");
        }

        @Override
        public int available() throws IOException {
            return super.available();
        }

        @Override
        public void close() throws IOException {
            super.close();
            forwardTo.close();
        }

        @Override
        public void mark(int readLimit) {
            throw new UnsupportedOperationException("Can't mark, sorry");
        }

        @Override
        public void reset() {
            throw new UnsupportedOperationException("Can't reset, sorry");
        }

        @Override
        public boolean markSupported() {
            return false;
        }
    }

    public static <T extends Serializable> byte[] serialize(T serializable) throws IOException {

        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
             final ObjectOutputStream out = new ObjectOutputStream(outputStream)) {

            out.writeObject(serializable);
            return outputStream.toByteArray();
        }
    }

    public static <T extends Serializable> T deserialize(InputStream inputStream) throws IOException {
        try (ObjectInputStream in = new ObjectInputStream(inputStream)) {
            //noinspection unchecked
            return (T) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static double distance(GeoPt p1, GeoPt p2) {

        double latitude = Math.toRadians((double) p1.getLatitude());
        double longitude = Math.toRadians((double) p1.getLongitude());
        double otherLatitude = Math.toRadians((double) p2.getLatitude());
        double otherLongitude = Math.toRadians((double) p2.getLongitude());
        double deltaLat = latitude - otherLatitude;
        double deltaLong = longitude - otherLongitude;
        double a1 = haversin(deltaLat);
        double a2 = Math.cos(latitude) * Math.cos(otherLatitude) * haversin(deltaLong);
        return 1.274202E7D * Math.asin(Math.sqrt(a1 + a2));
    }

    public static double haversin(double delta) {
        double x = Math.sin(delta / 2.0D);
        return x * x;
    }

    public static String getCustomerId(@Nullable User user) throws UnauthorizedException {

        if (user == null || TextUtils.isEmpty(user.getId()))
            throw new UnauthorizedException("Could not authorize");

        return user.getId();
    }

    public static String getProvider(@Nullable User user) throws UnauthorizedException {

        if (user == null || TextUtils.isEmpty(user.getEmail()))
            throw new UnauthorizedException("Could not authorize");

        return user.getEmail();
    }
}