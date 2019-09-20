/*
 * Copyright (c) 2008-2019, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.sql.impl.mailbox;

import com.hazelcast.sql.impl.QueryId;
import com.hazelcast.sql.impl.exec.Exec;

/**
 * Abstract inbox implementation.
 */
public abstract class AbstractInbox extends AbstractMailbox {
    /** Executor which should be notified when data arrives. */
    private Exec exec;

    /** Remaining remote sources. */
    private int remaining;

    protected AbstractInbox(QueryId queryId, int edgeId, int remaining) {
        super(queryId, edgeId);

        this.remaining = remaining;
    }

    /**
     * Handle batch arrival. Always invoked from the worker.
     */
    public void onBatch(String sourceMemberId, SendBatch batch) {
        onBatch0(sourceMemberId, batch);

        if (batch.isLast())
            remaining--;
    }

    protected abstract void onBatch0(String sourceMemberId, SendBatch batch);

    /**
     * @return {@code True} if no more incoming batches are expected.
     */
    public boolean closed() {
        return remaining == 0;
    }

    public Exec getExec() {
        return exec;
    }

    public void setExec(Exec exec) {
        this.exec = exec;
    }
}
