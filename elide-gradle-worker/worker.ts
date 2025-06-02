/// <reference types="@cloudflare/workers-types" />
/// <reference path="./worker-apis.d.ts" />

import { WorkerEntrypoint } from "cloudflare:workers"

const mainBranch = 'main';
const latestTag = 'latest';
const redirectStatus = 302;
const expectedScript = 'elide.gradle.kts'
const githubUrlBase = 'https://raw.githubusercontent.com/elide-dev/gradle';

enum CacheMode {
    Short,
    Long,
}

function renderCacheControl(mode: CacheMode): string {
    const base = `public`;
    switch (mode) {
        case CacheMode.Short:
            return `${base}, max-age=60, stale-while-revalidate=60`;
        case CacheMode.Long:
            return `${base}, max-age=604800, stale-while-revalidate=604800`;
        default:
            return base;
    }
}

// Entrypoint for the worker.
export default class extends WorkerEntrypoint<Env> {
    async fetch(request: Request): Promise<Response> {
        const url = new URL(request.url);
        const { pathname } = url;
        if (pathname === '/') {
            return new Response('NOT_FOUND', { status: 404 });
        }
        let revision: string = '';
        let type: string = '';
        let cacheMode: CacheMode = CacheMode.Short;

        if (pathname === `/${expectedScript}`) {
            revision = mainBranch;
            type = 'refs/heads';
        } else {
            // remove `/tag/` segment unconditionally to cover errant url format published briefly
            const firstSegment = (pathname.replace("/tag/", "/")).split('/')[1] || '';
            if (firstSegment === latestTag) {
                revision = mainBranch;
                type = 'refs/heads';
            }
            if (firstSegment === mainBranch) {
                revision = mainBranch;
                type = 'refs/heads';
            }
            if (firstSegment.indexOf('1.0') === 0) {
                // it's a version
                revision = firstSegment;
                type = 'refs/tags';
                cacheMode = CacheMode.Long;
            }
            if (!revision) {
                // we should assume it's a commit
                revision = firstSegment;
                type = 'refs';
                cacheMode = CacheMode.Long;
            }
        }
        const finalizedUrl = `${githubUrlBase}/${type}/${revision}/${expectedScript}`;
        const headers = new Headers();
        headers.set("Cache-Control", renderCacheControl(cacheMode));
        headers.set("Location", finalizedUrl);
        return new Response(null, { status: redirectStatus, headers })
    }
}
