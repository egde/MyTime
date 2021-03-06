package com.kazkazi.mytime.dbs;

import android.app.Application;
import android.os.AsyncTask;

import com.kazkazi.mytime.entities.Work;

import java.util.ArrayList;
import java.util.List;

public class WorkEntityRepo {
    private final WorkStoreDao dao;

    public WorkEntityRepo(Application application) {
        WorkStore db = WorkStore.getDatabase(application.getApplicationContext());
        dao = db.workStoreDao();
    }

    public void getAllWork(DBCallback<List<Work>> callback) {
        new GatAllWorkAsyncTask(dao, callback).execute();
    }
    public void getLatestWork(DBCallback<Work> callback) {
        new GetLatestWorkAsyncTask(dao, callback).execute();
    }

    public void insert (Work work, DBCallback<WorkStoreEntity> callback) {
        WorkStoreEntity workStoreEntity = new WorkStoreEntity(work);

        new InsertAsyncTask(dao, callback).execute(workStoreEntity);
    }

    public void update (Work work) {
        WorkStoreEntity workStoreEntity = new WorkStoreEntity(work);

        new UpdateAsyncTask(dao).execute(workStoreEntity);
    }

    private static class InsertAsyncTask extends AsyncTask<WorkStoreEntity, Void, WorkStoreEntity> {
        private final WorkStoreDao dao;
        private final DBCallback<WorkStoreEntity> callback;

        InsertAsyncTask(WorkStoreDao dao, DBCallback<WorkStoreEntity>  callback) {
            this.dao = dao;
            this.callback = callback;
        }

        @Override
        protected WorkStoreEntity doInBackground(final WorkStoreEntity... params) {
            dao.insert(params[0]);
            return params[0];
        }

        @Override
        protected void onPostExecute(WorkStoreEntity workStoreEntity) {
            super.onPostExecute(workStoreEntity);
            callback.onSuccess(workStoreEntity);
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<WorkStoreEntity, Void, Void> {
        private final WorkStoreDao dao;

        UpdateAsyncTask(WorkStoreDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(final WorkStoreEntity... params) {
            dao.update(params[0]);
            return null;
        }
    }

    private static class GetLatestWorkAsyncTask extends AsyncTask<Void, Void, WorkStoreEntity>{
        private final WorkStoreDao dao;
        private final DBCallback<Work> callback;

        GetLatestWorkAsyncTask(WorkStoreDao dao, DBCallback<Work> callback) {
            this.dao = dao;
            this.callback = callback;
        }

        @Override
        protected WorkStoreEntity doInBackground(final Void... params) {
            return dao.getLastWorkStoreEntity();
        }

        @Override
        protected void onPostExecute(WorkStoreEntity workStoreEntity) {
            super.onPostExecute(workStoreEntity);
            if (workStoreEntity == null || workStoreEntity.getData().isEmpty()) {
                callback.onSuccess(null);
            } else {
                Work w =Work.fromJson(workStoreEntity.getData());
                w.setId(workStoreEntity.getId());
                callback.onSuccess(w);
            }
        }
    }

    private static class GatAllWorkAsyncTask extends AsyncTask<Void, Void, List<WorkStoreEntity>>{
        private final WorkStoreDao dao;
        private final DBCallback<List<Work>> callback;

        public GatAllWorkAsyncTask(WorkStoreDao dao, DBCallback<List<Work>> callback) {
            this.dao = dao;
            this.callback = callback;
        }


        @Override
        protected List<WorkStoreEntity> doInBackground(Void... voids) {
            return dao.getAll();
        }

        @Override
        protected void onPostExecute(List<WorkStoreEntity>  workStoreEntityList) {
            super.onPostExecute(workStoreEntityList);
            if (workStoreEntityList == null || workStoreEntityList.isEmpty()) {
                callback.onSuccess(null);
            } else {
                ArrayList<Work> result = new ArrayList<>();
                for (WorkStoreEntity w : workStoreEntityList) {
                    result.add(Work.fromJson(w.getData()));
                }
                callback.onSuccess(result);
            }
        }
    }
}
