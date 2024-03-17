package com.example.final_project.repository;

import android.util.Log;

import com.example.final_project.data.SharedPreferencesDataSource;
import com.example.final_project.models.Company;
import com.example.final_project.models.Job;
import com.example.final_project.models.JobCategory;
import com.example.final_project.models.Resume;
import com.example.final_project.models.User;
import com.example.final_project.utils.GetJobResult;
import com.example.final_project.utils.JobDetailsFireStoreResult;
import com.example.final_project.utils.JobFireStoreResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class JobRepository {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static JobRepository instance;

    private JobRepository() {
    }

    public static JobRepository getInstance() {
        if (instance == null) {
            instance = new JobRepository();
        }
        return instance;
    }

    public User getUser() {
        return new Gson().fromJson(SharedPreferencesDataSource.getInstance().getString("user"), User.class);
    }

    public void fetchAppliedJobs(JobFireStoreResult result){
        User user = getUser();
        db.collection("applications").whereEqualTo("userId", user.getId()).get().addOnSuccessListener(task ->{
            List<CompletableFuture<Job>> futures = new ArrayList<>();
            for (int i = 0; i < task.size(); i++) {
                String jobId = task.getDocuments().get(i).getString("jobId");
                CompletableFuture<Job> future = getJobById(jobId);
                futures.add(future);
            }
            CompletableFuture<Void> allOf = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[0])
            );
            allOf.thenRun(() -> {
                List<Job> mapJobs = futures.stream()
                        .map(f -> {
                            try {
                                return f.get();
                            } catch (Exception e) {
                                return null;
                            }
                        })
                        .collect(Collectors.toList());
                result.onGetJobsByCompanyResult(true, mapJobs);
            }).exceptionally(e -> {
                result.onGetJobsByCompanyResult(false, null);
                return null;
            });
        }).addOnFailureListener(
            e -> {
                result.onGetJobsByCompanyResult(false, null);
            }
        );
    }

    public void applyJob(Job job, Resume resume, JobDetailsFireStoreResult result){
        User user = new Gson().fromJson(SharedPreferencesDataSource.getInstance().getString("user"), User.class);
        db.collection("applications").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, String> application = new HashMap<>();
                application.put("jobId", job.getId());
                application.put("userId", user.getId());
                application.put("resumeId", resume.getId());
                application.put("timestamp", String.valueOf(System.currentTimeMillis()));
                application.put("status", "pending");
                db.collection("applications").add(application).addOnSuccessListener(documentReference -> {
                    result.onApplyJobResult(true);
                }).addOnFailureListener(e -> {
                    result.onApplyJobResult(false);
                });
            } else {
                result.onApplyJobResult(false);
            }
        }).addOnFailureListener(e -> {
            result.onApplyJobResult(false);
        });
    }

    public void addJob(Job job, JobFireStoreResult results) {
        db.collection("jobs").add(job).addOnSuccessListener(documentReference -> {
            results.onAddJobResult(true);
        }).addOnFailureListener(e -> {
            results.onAddJobResult(false);
        });
    }

    public void getJobs(JobFireStoreResult results){
        db.collection("jobs").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<CompletableFuture<Job>> futures = new ArrayList<>();
                for (int i = 0; i < task.getResult().size(); i++) {
                    Job job = task.getResult().toObjects(Job.class).get(i);
                    job.setId(task.getResult().getDocuments().get(i).getId());
                    CompletableFuture<Job> future = new CompletableFuture<>();
                    futures.add(future);
                    db.collection("companies").document(job.getCompanyId()).get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task1.getResult();
                            if (documentSnapshot.exists()) {
                                job.setCompany(documentSnapshot.toObject(Company.class));
                                future.complete(job);
                            }
                        } else {
                            future.completeExceptionally(task1.getException());
                        }
                    }).addOnFailureListener(e -> {
                        future.completeExceptionally(e);
                    });
                }
                CompletableFuture<Void> allOf = CompletableFuture.allOf(
                        futures.toArray(new CompletableFuture[0])
                );
                allOf.thenRun(() -> {
                    List<Job> mapJobs = futures.stream()
                            .map(f -> {
                                try {
                                    return f.get();
                                } catch (Exception e) {
                                    return null;
                                }
                            })
                            .collect(Collectors.toList());

                    results.onGetJobsByCompanyResult(true, mapJobs);
                }).exceptionally(e -> {
                    results.onGetJobsByCompanyResult(false, null);
                    return null;
                });
            } else {
                results.onGetJobsByCompanyResult(false, null);
            }
        }).addOnFailureListener(e -> {
            results.onGetJobsByCompanyResult(false, null);
        });
    }

    public CompletableFuture<Job> getJobById(String jobId){
        CompletableFuture<Job> future = new CompletableFuture<>();
        db.collection("jobs").document(jobId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Job job = task.getResult().toObject(Job.class);
                job.setId(task.getResult().getId());
                db.collection("companies").document(job.getCompanyId()).get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task1.getResult();
                        if (documentSnapshot.exists()) {
                            job.setCompany(documentSnapshot.toObject(Company.class));
                            future.complete(job);
                        }
                    } else {
                        future.completeExceptionally(task1.getException());
                    }
                }).addOnFailureListener(e -> {
                    future.completeExceptionally(e);
                });
            } else {
                future.completeExceptionally(task.getException());
            }
        }).addOnFailureListener(e -> {
            future.completeExceptionally(e);
        });
        return future;
    }

    public void getJobsByCompany(String companyId, JobFireStoreResult results) {
        List<Job> jobs = new ArrayList<>();
        db.collection("jobs").whereEqualTo("companyId", companyId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<CompletableFuture<Job>> futures = new ArrayList<>();

                for (int i = 0; i < task.getResult().size(); i++) {
                    Job job = task.getResult().toObjects(Job.class).get(i);
                    job.setId(task.getResult().getDocuments().get(i).getId());
                    CompletableFuture<Job> future = new CompletableFuture<>();
                    db.collection("companies").document(job.getCompanyId()).get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task1.getResult();
                            if (documentSnapshot.exists()) {
                                job.setCompany(documentSnapshot.toObject(Company.class));
                                future.complete(job);
                            }
                        } else {
                            future.completeExceptionally(task1.getException());
                        }
                    }).addOnFailureListener(e -> {
                        future.completeExceptionally(e);
                    });
                    future.thenAcceptAsync(j -> {
                        jobs.add(j);
                    });
                    futures.add(future);
                }
                CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

                allOf.thenRun(() -> {
                    List<Job> mapJobs = futures.stream()
                            .map(f -> {
                                try {
                                    return f.get();
                                } catch (Exception e) {
                                    return null;
                                }
                            })
                            .collect(Collectors.toList());
                    results.onGetJobsByCompanyResult(true, mapJobs);
                }).exceptionally(e -> {
                    results.onGetJobsByCompanyResult(false, null);
                    return null;
                });
            } else {
                results.onGetJobsByCompanyResult(false, null);
            }
        }).addOnFailureListener(e -> {
            results.onGetJobsByCompanyResult(false, null);
        });
    }

    public Company getCompany() {
        return new Gson().fromJson(SharedPreferencesDataSource.getInstance().getString("company"), Company.class);
    }

    public void getJobCategories(final JobFireStoreResult results) {
        db.collection("jobCategories").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<JobCategory> jobCategories = new ArrayList<>();
                for (int i = 0; i < task.getResult().size(); i++) {
                    JobCategory category = task.getResult().toObjects(JobCategory.class).get(i);
                    category.setId(task.getResult().getDocuments().get(i).getId());
                    jobCategories.add(category);
                }
                results.onGetJobCatResult(true, jobCategories);
            } else {
                results.onGetJobCatResult(false, null);
            }
        }).addOnFailureListener(e -> {
            results.onGetJobCatResult(false, null);
        });
    }

    public void bookmarkJob(String jobId, String userId ,JobDetailsFireStoreResult result){
        db.collection("bookmarkJobs").whereEqualTo("jobId", jobId).whereEqualTo("userId", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().size() == 0) {
                    Map<String, String> bookmarkJob = new HashMap<>();
                    bookmarkJob.put("jobId", jobId);
                    bookmarkJob.put("userId", userId);
                    db.collection("bookmarkJobs").add(bookmarkJob).addOnSuccessListener(documentReference -> {
                        result.onBookmarkJobResult(true);
                    }).addOnFailureListener(e -> {
                        result.onBookmarkJobResult(false);
                    });
                } else {
                    result.onBookmarkJobResult(false);
                }
            } else {
                result.onBookmarkJobResult(false);
            }
        }).addOnFailureListener(e -> {
            result.onBookmarkJobResult(false);
        });
    }

    public void removeBookmark(String jobId, String userId ,JobDetailsFireStoreResult result){
        db.collection("bookmarkJobs").whereEqualTo("jobId", jobId).whereEqualTo("userId", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().size() > 0) {
                    db.collection("bookmarkJobs").document(task.getResult().getDocuments().get(0).getId()).delete().addOnSuccessListener(aVoid -> {
                        result.onBookmarkJobResult(true);
                    }).addOnFailureListener(e -> {
                        result.onBookmarkJobResult(false);
                    });
                } else {
                    result.onBookmarkJobResult(false);
                }
            } else {
                result.onBookmarkJobResult(false);
            }
        }).addOnFailureListener(e -> {
            result.onBookmarkJobResult(false);
        });
    }


    public void isJobBookmarked(String jobId, String userId, JobDetailsFireStoreResult result){
        db.collection("bookmarkJobs").whereEqualTo("jobId", jobId).whereEqualTo("userId", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().size() == 0) {
                    result.onBookmarkJobResult(false);
                } else {
                    result.onBookmarkJobResult(true);
                }
            } else {
                result.onBookmarkJobResult(false);
            }
        }).addOnFailureListener(e -> {
            result.onBookmarkJobResult(false);
        });
    }

    public void getBookmarkedJobs(String userId, JobFireStoreResult result){
        db.collection("bookmarkJobs").whereEqualTo("userId", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<CompletableFuture<Job>> futures = new ArrayList<>();
                for (int i = 0; i < task.getResult().size(); i++) {
                    String jobId = task.getResult().getDocuments().get(i).getString("jobId");
                    CompletableFuture<Job> future = getJobById(jobId);
                    futures.add(future);
                }
                CompletableFuture<Void> allOf = CompletableFuture.allOf(
                        futures.toArray(new CompletableFuture[0])
                );
                allOf.thenRun(() -> {
                    List<Job> mapJobs = futures.stream()
                            .map(f -> {
                                try {
                                    return f.get();
                                } catch (Exception e) {
                                    return null;
                                }
                            })
                            .collect(Collectors.toList());
                    result.onGetJobsByCompanyResult(true, mapJobs);
                }).exceptionally(e -> {
                    result.onGetJobsByCompanyResult(false, null);
                    return null;
                });
            } else {
                result.onGetJobsByCompanyResult(false, null);
            }
        }).addOnFailureListener(e -> {
            result.onGetJobsByCompanyResult(false, null);
        });
    }
}
