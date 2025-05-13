from setuptools import setup, find_packages

setup(
    name="sleep_api",
    version="1.0.0",
    packages=find_packages(),
    python_requires=">=3.10,<3.13",
    install_requires=[
        "fastapi==0.115.12",
        "uvicorn[standard]==0.22.0",
        "tensorflow-cpu==2.12.0",
        "numpy==1.23.5",
        "pydantic==2.6.2",
        "requests==2.31.0",
    ],
    entry_points={
        "console_scripts": [
            "sleep_api-serve = main:app",
        ],
    },
)
